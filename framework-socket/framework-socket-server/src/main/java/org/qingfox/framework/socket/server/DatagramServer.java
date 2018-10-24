package org.qingfox.framework.socket.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.qingfox.framework.socket.server.listener.IServerListener;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;

public class DatagramServer implements IServer<byte[]> {

	private static final ILogger logger = LoggerFactory.getLogger(DatagramServer.class);

	private DatagramSocket serverSocket;
	private Integer port;
	private Integer receiveSize;
	private List<IServerListener<byte[]>> serverListenerList;

	// private Map<String, DatagramSocket> clientSocketMap;

	public DatagramServer(Integer port) {
		this.port = port;
		receiveSize = 256;
		// clientSocketMap = new HashMap<String, DatagramSocket>();
		serverListenerList = new ArrayList<IServerListener<byte[]>>();
	}

	public void start() throws IOException {
		serverSocket = new DatagramSocket(port);
		createReceiveThread();

	}

	private void createReceiveThread() {
		new Thread() {
			@Override
			public void run() {
				DatagramPacket serverPacket = new DatagramPacket(new byte[receiveSize], receiveSize);
				while (true) {
					try {
						serverSocket.receive(serverPacket);
						byte[] data = serverPacket.getData();
						if (receiveSize > serverPacket.getLength()) {
							data = ArrayUtils.subarray(serverPacket.getData(), serverPacket.getOffset(), serverPacket.getLength());
						}
						String socketId = serverPacket.getAddress().getHostAddress() + "_" + serverPacket.getPort();
						createResponseReceiveMessageThread(socketId, data);
					} catch (IOException e) {
						logger.error(e, "接收信息失败");
						continue;
					}

				}
			}
		}.start();
	}

	private void createResponseReceiveMessageThread(String socketId, byte[] message) {
		for (IServerListener<byte[]> listener : serverListenerList) {
			new Thread() {
				private String socketId;
				private byte[] message;
				private IServerListener<byte[]> listener;

				public Thread setThreadInfo(String socketId, byte[] message, IServerListener<byte[]> listener) {
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
	public void stop() throws IOException {
		serverSocket.close();
	}

	@Override
	public void send(String socketId, byte[] message) {
		// DatagramSocket socket = clientSocketMap.get(socketId);
		// if (socket == null) {
		// String[] infos = socketId.split(",");
		// try {
		// InetAddress serverAddress = InetAddress.getByName(infos[0]);
		// socket = new DatagramSocket(Integer.parseInt(infos[1]),
		// serverAddress);
		// } catch (UnknownHostException | NumberFormatException |
		// SocketException e) {
		// logger.error(e, "创建socket服务失败");
		// return;
		// }
		String[] infos = socketId.split("_");
		try {
			InetAddress serverAddress = InetAddress.getByName(infos[0]);
			DatagramPacket sendPacket = new DatagramPacket(message, // 相当于将发送的信息打包
					message.length, serverAddress, Integer.parseInt(infos[1]));
			serverSocket.send(sendPacket);
		} catch (Exception e) {
			logger.error(e, "发送信息失败");
			return;
		}
	}

	@Override
	public void asynSend(byte[] message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addServerListener(IServerListener<byte[]> listener) {
		if (!serverListenerList.contains(listener)) {
			serverListenerList.add(listener);
		}
	}

	@Override
	public void send(String[] socketIds, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void asynSend(String socketId, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void asynSend(String[] socketIds, byte[] message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public byte[] sendCallback(String socketId, byte[] message) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCallbackTimeout(Long callbackTimeout) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getSocketIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
