package com.framework.common.enums;

public enum DateEnum {
	SECOND(1), MINUTE(2), HOUR(3), DAY(4), WEEK(5), MONTH(6), YEAR(7);

	private Integer value;

	DateEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static DateEnum getEnmu(Integer value) {

		if (value != null) {
			for (DateEnum _enum : DateEnum.values()) {
				if (_enum.getValue().equals(value)) {
					return _enum;
				}
			}
		}
		return null;

	}

}
