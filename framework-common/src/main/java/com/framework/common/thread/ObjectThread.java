package com.framework.common.thread;

public class ObjectThread<T> extends Thread {
	private T object;

	public T getObject() {
		return object;
	}

	public ObjectThread<T> setObject(T object) {
		this.object = object;
		return this;
	}
}
