package org.qingfox.framework.common.beans;

import java.io.Serializable;

public class SimpleLabel<N, V> implements Serializable {

	private static final long serialVersionUID = 1362347138172661149L;

	private N name;
	private V value;

	public SimpleLabel() {

	}

	public SimpleLabel(N name, V value) {
		this.name = name;
		this.value = value;
	}

	public N getName() {
		return name;
	}

	public void setName(N name) {
		this.name = name;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
