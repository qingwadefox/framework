package com.framework.plugin.web.common.server.response.basic;

import java.io.Serializable;

public class BasicResponse implements Serializable {

    private static final long serialVersionUID = -3230666763612357873L;

    public static int WEB_MODE = 1;
    public static int CLIENT_MODE = 2;

    private int mode = WEB_MODE;
    private String errorcode;
    private boolean success;
    private String message;
    private String url;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

}
