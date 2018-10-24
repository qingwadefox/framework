package org.qingfox.framework.socket.client.listener;

public interface IClientListener<T> {

	public void onReceive(T message);

	public void onDisconnect();
}
