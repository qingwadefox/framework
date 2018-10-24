package com.framework.common.beans;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1304799150230090132L;

	private Boolean success = true;
	private Integer code;
	private T result;
	private String message;
	private Long session;

	public Result() {
		session = System.currentTimeMillis();
	}

	public Result(Long session) {
		this.session = session;
	}

	public void setSession(Long session) {
		this.session = session;
	}

	public Long getSession() {
		return session;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(Object... messages) {
		this.message = StringUtils.join(messages);
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public VoidResult toVoidResult() {
		VoidResult voidResult = new VoidResult();
		copyResult(voidResult);
		return voidResult;
	}

	public Result<T> toResult(T resultClass) {
		Result<T> result = new Result<T>(session);
		result.setMessage(message);
		result.setSuccess(success);
		result.setCode(code);
		return result;
	}

	public void copyResult(Result<?> result) {
		this.session = result.getSession();
		this.message = result.message;
		this.success = result.success;
		this.code = result.code;
	}

}
