package com.framework.plugin.web.common.server.exception;

import com.framework.plugin.web.common.server.enums.ExceptionEnum;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = -1802505120335918539L;

    public ExceptionEnum eEnum;
    public String url;

    public ServiceException(ExceptionEnum eEnum) {
        super(eEnum.getMsgcn());
        this.eEnum = eEnum;
    }

    public ServiceException(ExceptionEnum eEnum, String url) {
        super(eEnum.getMsgcn());
        this.eEnum = eEnum;
        this.url = url;
    }

    public ServiceException(ExceptionEnum eEnum, Exception cause) {
        super(eEnum.getMsgcn(), cause);
        this.eEnum = eEnum;
    }

    public ExceptionEnum geteEnum() {
        return eEnum;
    }

    public void seteEnum(ExceptionEnum eEnum) {
        this.eEnum = eEnum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
