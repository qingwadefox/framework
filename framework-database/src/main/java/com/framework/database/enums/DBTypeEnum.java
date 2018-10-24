package com.framework.database.enums;

public enum DBTypeEnum {

	ORACLE(0), MYSQL(1), MSSQL(2);

	private Integer value;

	DBTypeEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return this.value;
	}

	public static DBTypeEnum getEnmu(Integer value) {

		if (value != null) {
			for (DBTypeEnum _enum : DBTypeEnum.values()) {
				if (_enum.getValue().equals(value)) {
					return _enum;
				}
			}
		}
		return ORACLE;

	}
}
