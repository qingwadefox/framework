package com.framework.plugin.web.common.server.enums;

public enum ExceptionEnum {

    // 登陆相关
    LOGINUNKNOW("LOGIN-0", "未登录系统", ""), LOGINFAILURE("LOGIN-1", "登陆失败", ""),

    // 权限相关
    POWERUNKNOW("POWER-0", "无功能权限", ""),

    // 过滤器相关
    FILTERUNKNOW("FILTER-0", "相关功能无法通过", ""),

    // APP访问现骨干
    APPWRONGPARAM("APP-0", "app参数访问出错", ""),

    // 文件相关
    FILEUNKNOW("FILE-0", "文件不存在", ""), FILEDOWNLOADERROR("FILE-1", "文件下载失败", "");

    private String code;
    private String msgcn;
    private String msgen;

    private ExceptionEnum(String code, String msgcn, String msgen) {
        this.code = code;
        this.msgcn = msgcn;
        this.msgen = msgen;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsgcn() {
        return msgcn;
    }

    public void setMsgcn(String msgcn) {
        this.msgcn = msgcn;
    }

    public String getMsgen() {
        return msgen;
    }

    public void setMsgen(String msgen) {
        this.msgen = msgen;
    }

}
