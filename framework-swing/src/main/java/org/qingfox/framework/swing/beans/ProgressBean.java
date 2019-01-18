package org.qingfox.framework.swing.beans;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class ProgressBean implements Serializable {

	private static final long serialVersionUID = -858977342945564025L;
	private Integer max = 100;
	private Integer number = 0;
	private String message = null;
	private Boolean close = false;

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setMessage(Object... messages) {
		this.message = StringUtils.join(messages);
	}

	public Boolean getClose() {
		return close;
	}

	public void setClose(Boolean close) {
		this.close = close;
	}

}
