package com.framework.socket.api;

public class DataMessage extends Message<byte[]> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8498002982903295573L;
	private Integer length;

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}
