package org.qingfox.framework.socket.server.listener;

public interface IServerListener<T> {

	public void onReceive(String socketId, T message);

	public void onClientDisconnect(String socketId);
}
