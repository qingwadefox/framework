package com.framework.socket.api;

public class ProxyMessage extends Message<Void> {

	private static final long serialVersionUID = 4942092913283507301L;

	private Object[] args;

	private Class<?>[] argTypes;

	private Object result;

	private String methodName;

	private boolean callBack = false;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Class<?>[] getArgTypes() {
		return argTypes;
	}

	public void setArgTypes(Class<?>[] argTypes) {
		this.argTypes = argTypes;
	}

	public boolean isCallBack() {
		return callBack;
	}

	public void setCallBack(boolean callBack) {
		this.callBack = callBack;
	}

}
