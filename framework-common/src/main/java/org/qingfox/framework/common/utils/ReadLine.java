package org.qingfox.framework.common.utils;

public abstract class ReadLine<T> {

	private T param;

	public abstract void nextLine(String line, int number);

	public ReadLine<T> setParam(T param) {
		this.param = param;
		return this;
	}

	public T getParam() {
		return param;
	}
}
