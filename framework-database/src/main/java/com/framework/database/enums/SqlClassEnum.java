package com.framework.database.enums;

import org.apache.commons.lang3.ArrayUtils;

public enum SqlClassEnum {

	// 时间
	DATE(new String[] { "TIMESTAMP(6)", "timestamp" }),

	// 字符串
	STRING(new String[] { "VARCHAR2", "VARCHAR" }),

	// 数字
	NUMBER(new String[] { "NUMBER" });

	private String[] value;

	SqlClassEnum(String[] value) {
		this.value = value;
	}

	public String[] getValue() {
		return value;
	}

	public void setValue(String[] value) {
		this.value = value;
	}

	public static SqlClassEnum getEnmu(String value) {
		if (value != null) {
			for (SqlClassEnum _enum : SqlClassEnum.values()) {
				if (ArrayUtils.indexOf(_enum.getValue(), value) != -1) {
					return _enum;
				}
			}
		}
		return STRING;
	}

}
