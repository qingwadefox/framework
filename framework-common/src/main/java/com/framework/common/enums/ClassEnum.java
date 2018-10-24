package com.framework.common.enums;

public enum ClassEnum {

	// InputStream
	IS("java.io.InputStream"),

	// String
	STRING("java.lang.String"),

	// TIMESTAMP
	ORLTIMESTAMP("oracle.sql.TIMESTAMP"),

	// TIMESTAMP
	SQLTIMESTAMP("java.sql.Timestamp"),

	// DATE
	DATE("java.util.Date"),

	// Calendar
	CALENDAR("java.util.GregorianCalendar"),

	// SQLDATE
	SQLDATE("java.sql.Date"),

	// BigDecimal
	BIGDECIMAL("java.math.BigDecimal"),

	// Integer
	INTEGER("java.lang.Integer"),

	// Long
	LONG("java.lang.Long"),

	// Float
	FLOAT("java.lang.Float"),

	// Double
	DOUBLE("java.lang.Double"),

	// COLOR
	COLOR("java.awt.Color");

	private String value;

	ClassEnum(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static ClassEnum getEnmu(String value) {

		if (value != null) {
			for (ClassEnum _enum : ClassEnum.values()) {
				if (_enum.getValue().equals(value)) {
					return _enum;
				}
			}
		}
		return null;
	}

}
