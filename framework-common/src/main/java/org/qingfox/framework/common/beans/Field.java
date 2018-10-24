package org.qingfox.framework.common.beans;

import java.io.Serializable;
import java.util.Objects;

public class Field<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6891777131714470219L;
	private String name;
	private String code;
	private T value;

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

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public boolean equalsField(Field<T> field) {
		if (this == field)
			return true;
		if (field == null || getClass() != field.getClass())
			return false;
		return Objects.equals(code, field.code) && Objects.equals(name, field.name) && Objects.equals(value, field.value);
	}

}
