package org.qingfox.framework.socket.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.socket.api.Message;
import org.qingfox.framework.socket.api.ProxyMessage;
import org.qingfox.framework.socket.server.listener.IServerListener;

public class StreamServer implements IServer<Message<?>> {

	private static final ILogger logger = LoggerFactory.getLogger(StreamServer.class);

	private ServerSocket serverSocket;
	private Integer port;
	private Map<String, Socket> clientSocketMap;
	private Map<String, ObjectOutputStream> clientOutputStreamMap;
	private List<IServerListener<Message<?>>> serverListenerList;
	private List<Long> sessionList;
	private Map<Long, Message<?>> messageMap;
	private long callbackSleepTime = 200;
	private Long callbackTimeout = 10000L;

	public StreamServer(Integer port) {
		this.port = port;
		clientSocketMap = new HashMap<String, Socket>();
		serverListenerList = new ArrayList<IServerListener<Message<?>>>();
		sessionList = new ArrayList<Long>();
		messageMap = new HashMap<Long, Message<?>>();
		clientOutputStreamMap = new HashMap<String, ObjectOutputStream>();
	}

	@Override
	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
		createAcceptThread();
	}

	@Override
	public void stop() throws IOException {

		if (!serverSocket.isClosed()) {
			Iterator<Entry<String, Socket>> it = clientSocketMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Socket> entry = it.next();
				try {
					entry.getValue().close();
				} catch (IOException e) {
					logger.error(e, "关闭客户端【", entry.getKey(), "】连接：失败");
				}
			}
			serverSocket.close();
		}
	}

	@Override
	public void addServerListener(IServerListener<Message<?>> listener) {
		serverListenerList.add(listener);
	}

	private void createAcceptThread() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Socket socket = serverSocket.accept();
						String socketId = System.currentTimeMillis() + "";
						clientSocketMap.put(socketId, socket);
						clientOutputStreamMap.put(socketId, new ObjectOutputStream(socket.getOutputStream()));
						createReceiveThread(socketId);
					} catch (IOException e) {
						logger.error(e, "接收客户端登录：失败");
						break;
					}
				}
			}
		}.start();
	}

	private void createReceiveThread(String socketId) {
		new Thread() {
			private String socketId;
			private Socket socket;

			public Thread setSocketId(String socketId) {
				this.socketId = socketId;
				this.socket = clientSocketMap.get(socketId);
				return this;
			}

			@Override
			public void run() {
				while (true) {
					try {
						ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
						Object object = ois.readObject();
						if (object.getClass().equals(Message.class) || object.getClass().equals(ProxyMessage.class)) {
							Message<?> message = (Message<?>) object;
							if (sessionList.contains(message.getSession())) {
								messageMap.put(message.getSession(), message);
								sessionList.remove(message.getSession());
							} else {
								createResponseReceiveMessageThread(socketId, message);
							}
						} else {
							logger.error("接收客户端信息不是API定义的message类型：失败");
						}
					} catch (Exception e) {
						logger.error(e, "客户端已经关闭连接");
						break;
					}
				}
				for (IServerListener<Message<?>> listener : serverListenerList) {
					listener.onClientDisconnect(socketId);
				}
				try {
					clientOutputStreamMap.get(socketId).close();
				} catch (IOException e) {
					logger.error(e, "关闭客户端传输流失败");
				}
				clientOutputStreamMap.remove(socketId);
				clientSocketMap.remove(socketId);

			}
		}.setSocketId(socketId).start();
	}

	private void createResponseReceiveMessageThread(String socketId, Message<?> message) {
		for (IServerListener<Message<?>> listener : serverListenerList) {
			new Thread() {
				private String socketId;
				private Message<?> message;
				private IServerListener<Message<?>> listener;

				public Thread setThreadInfo(String socketId, Message<?> message, IServerListener<Message<?>> listener) {
					this.socketId = socketId;
					this.message = message;
					this.listener = listener;
					return this;
				}

				@Override
				public void run() {
					listener.onReceive(socketId, message);
				}
			}.setThreadInfo(socketId, message, listener).start();
		}
	}

	@Override
	public void send(String socketId, Message<?> message) throws IOException {
		ObjectOutputStream oos = clientOutputStreamMap.get(socketId);
		oos.writeObject(message);
		oos.flush();
	}

	@Override
	public void asynSend(String socketId, Message<?> message) {
		ObjectOutputStream oos = clientOutputStreamMap.get(socketId);
		createSendThread(oos, message);
	}

	@Override
	public void send(Message<?> message) throws IOException {
		for (ObjectOutputStream oos : clientOutputStreamMap.values()) {
			oos.writeObject(message);
			oos.flush();
		}
	}

	@Override
	public void asynSend(Message<?> message) {
		for (ObjectOutputStream oos : clientOutputStreamMap.values()) {
			createSendThread(oos, message);
		}
	}

	private void createSendThread(ObjectOutputStream oos, Message<?> message) {
		try {
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			logger.error(e, "发送信息：失败");
		}
		// new Thread() {
		// private ObjectOutputStream oos;
		// private Message<?> message;
		//
		// public Thread setThreadInfo(ObjectOutputStream oos, Message<?>
		// message) {
		// this.oos = oos;
		// this.message = message;
		// return this;
		// }
		//
		// @Override
		// public void run() {
		//
		// }
		//
		// }.setThreadInfo(oos, message).start();
	}

	@Override
	public void send(String[] socketIds, Message<?> message) throws IOException {
		for (String socketId : socketIds) {
			this.send(socketId, message);
		}
	}

	@Override
	public void asynSend(String[] socketIds, Message<?> message) {
		for (String socketId : socketIds) {
			this.asynSend(socketId, message);
		}
	}

	@Override
	public Message<?> sendCallback(String socketId, Message<?> message) throws IOException {
		sessionList.add(message.getSession());
		send(message);
		long timeOutTime = callbackTimeout / callbackSleepTime;
		long nowTime = 0;
		boolean hasSession = false;
		while (nowTime < timeOutTime && !hasSession) {

			try {
				Thread.sleep(callbackSleepTime);
			} catch (InterruptedException e) {
				logger.error(e, "请求睡眠：失败");
			}

			Message<?> _message = messageMap.get(message.getSession());
			if (_message != null) {
				logger.info("获取到服务端返回信息");
				hasSession = true;
				message = _message;
				messageMap.remove(message.getSession());
				break;
			}
			nowTime++;
		}

		if (!hasSession) {
			logger.error("等待服务端返回信息超时：失败");
			message.setSuccess(false);
		}

		return message;
	}

	@Override
	public void setCallbackTimeout(Long callbackTimeout) {
		this.callbackTimeout = callbackTimeout;
	}

	@Override
	public String[] getSocketIds() {
		String[] socketIds = new String[clientSocketMap.size()];
		int i = 0;
		Iterator<Entry<String, Socket>> it = clientSocketMap.entrySet().iterator();
		while (it.hasNext()) {
			socketIds[i] = it.next().getKey();
			i++;
		}
		return socketIds;
	}

}
