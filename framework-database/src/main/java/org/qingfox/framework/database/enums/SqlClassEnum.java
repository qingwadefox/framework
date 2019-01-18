package org.qingfox.framework.database.enums;

import org.apache.commons.lang3.ArrayUtils;

public enum SqlClassEnum {

    // 时间
    DATE(new String[] { "TIMESTAMP" }),

    // 字符串
    STRING(new String[] { "VARCHAR", "TEXT" }),

    // 数字
    NUMBER(new String[] { "NUMBER", "TINYINT", "INT" });

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
                if (ArrayUtils.indexOf(_enum.getValue(), value.toUpperCase()) != -1) {
                    return _enum;
                }
            }
        }
        return STRING;
    }

}
