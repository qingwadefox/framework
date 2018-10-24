package org.qingfox.framework.socket.server;

import java.io.IOException;

import org.qingfox.framework.socket.server.listener.IServerListener;

public interface IServer<T> {
	/**
	 * 启动 服务
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void start() throws IOException, InterruptedException;

	/**
	 * 关闭服务
	 * 
	 * @throws IOException
	 */
	public void stop() throws IOException;

	/**
	 * 发送消息（指定客户端）
	 * 
	 * @param socketId
	 * @param message
	 * @throws IOException
	 */
	public void send(String socketId, T message) throws IOException;

	/**
	 * 异步发送（指定客户端）
	 * 
	 * @param socketId
	 * @param message
	 */
	public void asynSend(String socketId, T message);

	/**
	 * 发送消息（指定多个客户端）
	 * 
	 * @param socketIds
	 * @param message
	 * @throws IOException
	 */
	public void send(String[] socketIds, T message) throws IOException;

	/**
	 * 异步发送消息（指定多个客户端）
	 * 
	 * @param socketIds
	 * @param message
	 * @throws IOException
	 */
	public void asynSend(String[] socketIds, T message);

	/**
	 * 异步发送消息（所有客户端）
	 * 
	 * @param message
	 */
	public void asynSend(T message);

	/**
	 * 发送消息（所有客户端）
	 * 
	 * @param message
	 */
	public void send(T message) throws IOException;

	/**
	 * 新增服务器监听
	 * 
	 * @param listener
	 */
	public void addServerListener(IServerListener<T> listener);

	/**
	 * 发送信息等待返回
	 * 
	 * @param socketId
	 * @param message
	 * @return
	 */
	public T sendCallback(String socketId, T message) throws IOException;

	/**
	 * 设置超时时间
	 * 
	 * @param callbackTimeout
	 */
	public void setCallbackTimeout(Long callbackTimeout);

	/**
	 * 获取客户端列表
	 * 
	 * @return
	 */
	public String[] getSocketIds();
}
