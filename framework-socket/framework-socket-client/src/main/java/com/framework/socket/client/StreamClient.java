package com.framework.socket.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.socket.api.Message;
import com.framework.socket.api.ProxyMessage;
import com.framework.socket.client.listener.IClientListener;

public class StreamClient implements IClient<Message<?>> {

	private static final ILogger logger = LoggerFactory.getLogger(StreamClient.class);
	private String host;
	private Integer port;
	private Socket socket;
	private List<IClientListener<Message<?>>> clientListenerList;
	private List<Long> sessionList;
	private Map<Long, Message<?>> messageMap;
	private long callbackSleepTime = 200;
	private Long callbackTimeout = 10000L;

	public StreamClient(String host, Integer port) {
		this.host = host;
		this.port = port;
		clientListenerList = new ArrayList<IClientListener<Message<?>>>();
		sessionList = new ArrayList<Long>();
		messageMap = new HashMap<Long, Message<?>>();
	}

	@Override
	public void connect() throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		createReceiveThread();
	}

	private void createReceiveThread() {
		new Thread() {
			@Override
			public void run() {

				try {
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					while (true) {
						Object object = ois.readObject();
						if (object.getClass().equals(Message.class) || object.getClass().equals(ProxyMessage.class)) {
							Message<?> message = (Message<?>) object;
							if (sessionList.contains(message.getSession())) {
								messageMap.put(message.getSession(), message);
								sessionList.remove(message.getSession());
							} else {
								createResponseReceiveMessageThread((Message<?>) object);
							}
						} else {
							logger.error("接收服务端信息不是API定义的message类型：失败");
						}

					}
				} catch (Exception e) {
					logger.error(e, "读取数据包出错，关闭连接");
					try {
						close();
					} catch (IOException e1) {
						logger.error(e1, "关闭连接出错");
					}
				}
				for (IClientListener<Message<?>> listener : clientListenerList) {
					listener.onDisconnect();
				}
			}
		}.start();
	}

	private void createResponseReceiveMessageThread(Message<?> message) {
		for (IClientListener<Message<?>> listener : clientListenerList) {
			new Thread() {
				private Message<?> message;
				private IClientListener<Message<?>> listener;

				public Thread setThreadInfo(Message<?> message, IClientListener<Message<?>> listener) {
					this.message = message;
					this.listener = listener;
					return this;
				}

				@Override
				public void run() {
					listener.onReceive(message);
				}
			}.setThreadInfo(message, listener).start();
		}
	}

	@Override
	public void send(Message<?> message) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(message);
			oos.flush();
		} catch (IOException e) {
			logger.error(e, "发送信息：失败");
		}
	}

	@Override
	public Message<?> sendCallback(Message<?> message) throws IOException {
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
	public void addClientListener(IClientListener<Message<?>> listener) {
		if (!clientListenerList.contains(listener)) {
			clientListenerList.add(listener);
		}
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public void removeClientListener(IClientListener<Message<?>> listener) {
		clientListenerList.remove(listener);
	}

	@Override
	public void setCallbackTimeout(Long callbackTimeout) {
		this.callbackTimeout = callbackTimeout;
	}

}
