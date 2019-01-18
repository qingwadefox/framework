package org.qingfox.framework.socket.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.qingfox.framework.socket.client.listener.IClientListener;

import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;

public class DatagramClient implements IClient<byte[]> {

	private static final ILogger logger = LoggerFactory.getLogger(DatagramClient.class);

	private DatagramSocket clientSocket;
	private String ip;
	private Integer serverPort;
	private Integer receiveSize;
	private List<IClientListener<byte[]>> clientListenerList;
	private InetAddress serverAddress;

	// private Map<String, DatagramSocket> clientSocketMap;

	public DatagramClient(String ip, Integer serverPort) {
		this.ip = ip;
		this.serverPort = serverPort;
		receiveSize = 200000;
		// clientSocketMap = new HashMap<String, DatagramSocket>();
		clientListenerList = new ArrayList<IClientListener<byte[]>>();
	}

	@Override
	public void connect() throws IOException {
		clientSocket = new DatagramSocket();
		serverAddress = InetAddress.getByName(ip);
		createReceiveThread();

	}

	private void createReceiveThread() {
		new Thread() {
			@Override
			public void run() {
				DatagramPacket clientPacket = new DatagramPacket(new byte[receiveSize], receiveSize);
				while (true) {
					try {
						clientSocket.receive(clientPacket);
						byte[] data = clientPacket.getData();
						if (receiveSize > clientPacket.getLength()) {
							data = ArrayUtils.subarray(clientPacket.getData(), clientPacket.getOffset(), clientPacket.getLength());
						}
						createResponseReceiveMessageThread(data);
					} catch (IOException e) {
						logger.error(e, "接收信息失败");
						continue;
					}

				}
			}
		}.start();
	}

	private void createResponseReceiveMessageThread(byte[] message) {
		for (IClientListener<byte[]> listener : clientListenerList) {
			listener.onReceive(message);
		}
	}

	@Override
	public void close() throws IOException {
		clientSocket.close();
	}

	@Override
	public void send(byte[] message) {
		// DatagramSocket socket = clientSocketMap.get(socketId);
		// if (socket == null) {
		// String[] infos = socketId.split(",");
		// try {
		// InetAddress ClientAddress = InetAddress.getByName(infos[0]);
		// socket = new DatagramSocket(Integer.parseInt(infos[1]),
		// ClientAddress);
		// } catch (UnknownHostException | NumberFormatException |
		// SocketException e) {
		// logger.error(e, "创建socket服务失败");
		// return;
		// }
		try {
			DatagramPacket sendPacket = new DatagramPacket(message, // 相当于将发送的信息打包
					message.length, serverAddress, serverPort);
			clientSocket.send(sendPacket);
		} catch (Exception e) {
			logger.error(e, "发送信息失败");
			return;
		}
	}

	@Override
	public void addClientListener(IClientListener<byte[]> listener) {
		if (!clientListenerList.contains(listener)) {
			clientListenerList.add(listener);
		}
	}

	@Override
	public byte[] sendCallback(byte[] message) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeClientListener(IClientListener<byte[]> listener) {
		clientListenerList.remove(listener);
	}

	@Override
	public void setCallbackTimeout(Long callbackTimeout) {
		// TODO Auto-generated method stub
		
	}

}
