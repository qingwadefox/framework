package org.qingfox.framework.common.enums;

public enum EscapeEnum {
    // 当前时间
    NOWDATE("{NOWDATE}"),
    // 空
    NULL("{NULL}");

    private String value;

    EscapeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
