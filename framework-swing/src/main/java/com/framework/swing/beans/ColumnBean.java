package com.framework.swing.beans;

import java.io.Serializable;

public class ColumnBean implements Serializable {

	private static final long serialVersionUID = 3497359049881050298L;

	private String name;

	private String code;

	public ColumnBean() {

	}

	public ColumnBean(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
