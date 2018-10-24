package org.qingfox.framework.socket.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import org.qingfox.framework.socket.api.Message;
import org.qingfox.framework.socket.api.ProxyMessage;
import org.qingfox.framework.socket.client.listener.IClientListener;

public class ClientProxy<T> implements InvocationHandler, IClientListener<Message<?>> {
  private static final ILogger logger = LoggerFactory.getLogger(ClientProxy.class);
  private IClient<Message<?>> client;
  private T target;
  private List<String> methodList;

  public void bindServer(IClient<Message<?>> client) {
    this.client = client;
    this.client.addClientListener(this);
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
      if (method.getReturnType() == null || method.getReturnType().equals(void.class) || method.getReturnType().equals(Void.class)) {
        this.client.send(message);
        return null;
      } else {
        message = (ProxyMessage) this.client.sendCallback(message);
        return message.getResult();
      }
    } else {
      return method.invoke(target, args);
    }
  }

  @Override
  public void onReceive(Message<?> message) {
    if (message.getClass().equals(ProxyMessage.class)) {
      ProxyMessage proxyMessage = (ProxyMessage) message;
      String methodName = proxyMessage.getMethodName();
      Object[] args = proxyMessage.getArgs();
      Class<?>[] argTypes = proxyMessage.getArgTypes();
      Method method = null;
      try {
        method = this.target.getClass().getMethod(methodName, argTypes);
        method.setAccessible(true);
        method.invoke(target, args);
      } catch (Exception e) {
        logger.error(e, "执行代理方法出错methodName【", methodName, "】，argClasses【", argTypes, "】");
      }

      if (!proxyMessage.isCallBack() && method.getReturnType() != null && !method.getReturnType().equals(void.class) && !method.getReturnType().equals(Void.class)) {
        proxyMessage.setCallBack(true);
        this.client.send(proxyMessage);
      }
    } else {
      logger.error("非代理标准的信息类型messageClass【", message.getClass(), "】");
    }
  }

  @Override
  public void onDisconnect() {

  }

}
