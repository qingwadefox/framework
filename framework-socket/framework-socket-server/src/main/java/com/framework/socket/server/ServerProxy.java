package com.framework.socket.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.socket.api.Message;
import com.framework.socket.api.ProxyMessage;
import com.framework.socket.api.SocketId;
import com.framework.socket.server.listener.IServerListener;

public class ServerProxy<T> implements InvocationHandler, IServerListener<Message<?>> {
	private static final ILogger logger = LoggerFactory.getLogger(ServerProxy.class);
	private IServer<Message<?>> server;
	private T target;
	private List<String> methodList;

	public void bindServer(IServer<Message<?>> server) {
		this.server = server;
		this.server.addServerListener(this);
	}

	@SuppressWarnings("unchecked")
	public T bind(T target) {
		this.target = target;
		methodList = new ArrayList<String>();
		for (Method method : target.getClass().getDeclaredMethods()) {
			if (!methodList.contains(method.getName())) {
				methodList.add(method.getName());
			}
		}
		return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this); // 要绑定接口(这是一个缺陷，cglib弥补了这一缺陷)

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (methodList.contains(method.getName())) {
			ProxyMessage message = new ProxyMessage();
			message.setArgs(args);
			message.setArgTypes(method.getParameterTypes());
			message.setMethodName(method.getName());
			String[] socketIds = null;

			for (Object arg : args) {
				if (arg != null && arg.getClass().equals(SocketId.class)) {
					socketIds = ((SocketId) arg).gets();
				}
			}
			if (socketIds != null && socketIds.length > 0) {
				this.server.asynSend(socketIds, message);
			} else {
				this.server.asynSend(message);
			}

			return null;
		} else {
			return method.invoke(target, args);
		}

	}

	@Override
	public void onReceive(String socketId, Message<?> message) {
		if (message.getClass().equals(ProxyMessage.class)) {
			ProxyMessage proxyMessage = (ProxyMessage) message;
			String methodName = proxyMessage.getMethodName();
			Object[] args = proxyMessage.getArgs();
			Class<?>[] argTypes = proxyMessage.getArgTypes();
			SocketId socketIdObj = SocketId.create(socketId);
			Method method = null;
			try {
				method = this.target.getClass().getMethod(methodName, argTypes);
				for (int i = 0; i < argTypes.length; i++) {
					if (argTypes[i].equals(SocketId.class)) {
						if (socketIdObj == null) {
							socketIdObj = SocketId.create(socketId);
						}
						args[i] = socketIdObj;
					}
				}
				Object result = method.invoke(target, args);
				if (result != null) {
					proxyMessage.setResult(result);
				}
			} catch (Exception e) {
				logger.error(e, "执行代理方法出错methodName【", methodName, "】、argTypes【", argTypes, "】");
				proxyMessage.setSuccess(false);
			}

			if (!proxyMessage.isCallBack() && method.getReturnType() != null && !method.getReturnType().equals(void.class) && !method.getReturnType().equals(Void.class)) {
				proxyMessage.setCallBack(true);
				this.server.asynSend(socketId, proxyMessage);
			}
		} else {
			logger.error("非代理标准的信息类型messageClass【", message.getClass(), "】");
		}
	}

	@Override
	public void onClientDisconnect(String socketId) {
		// TODO Auto-generated method stub

	}
}
