package org.qingfox.framework.database.enums;

public enum ConditionEnum {

	// !=
	NOTEQ(0),

	// ==
	EQ(1),

	// %%
	LIKE(2),

	// <
	LT(3),

	// <=
	LE(4),

	// is not null
	NOTNULL(5),

	// is null
	ISNULL(6),

	// >
	GT(7),

	// >=
	GE(8),

	// ?%
	LLIKE(9),

	// %?
	RLIKE(10),

	// in ()
	IN(11),

	// 今日
	TODAY(12),

	// treeeq
	TREEEQ(13),

	// treein
	TREEIN(14);

	private Integer value;

	ConditionEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public static ConditionEnum getEnmu(Integer value) {

		if (value != null) {
			for (ConditionEnum _enum : ConditionEnum.values()) {
				if (_enum.getValue().equals(value)) {
					return _enum;
				}
			}
		}
		return EQ;

	}
}
