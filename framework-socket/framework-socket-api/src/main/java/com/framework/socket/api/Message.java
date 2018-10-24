package com.framework.socket.api;

import java.io.Serializable;

public class Message<T> implements Serializable {
	private static final long serialVersionUID = 7826022797055616948L;

	private Long session;

	private Boolean success = true;

	private String code;

	private String info;

	private T object;

	public Message() {
		session = System.currentTimeMillis();
	}

	public Long getSession() {
		return session;
	}

	public void setSession(Long session) {
		this.session = session;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
