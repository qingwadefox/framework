package com.framework.socket.client;

import java.io.IOException;
import java.net.UnknownHostException;

import com.framework.socket.client.listener.IClientListener;

public interface IClient<T> {
	/**
	 * 连接服务器
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void connect() throws UnknownHostException, IOException, InterruptedException;

	/**
	 * 关闭连接
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * 发送信息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void send(T message);

	/**
	 * 发送信息等待返回
	 * 
	 * @param message
	 * @return
	 * @throws IOException
	 */
	public T sendCallback(T message) throws IOException;

	/**
	 * 增加客户端监听
	 * 
	 * @param listener
	 */
	public void addClientListener(IClientListener<T> listener);

	/**
	 * 删除客户端监听
	 * 
	 * @param listener
	 */
	public void removeClientListener(IClientListener<T> listener);

	/**
	 * 设置超时时间
	 * 
	 * @param callbackTimeout
	 */
	public void setCallbackTimeout(Long callbackTimeout);
}
