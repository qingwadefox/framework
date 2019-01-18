package org.qingfox.framework.microservice.exception;

public class ServiceRegisterException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -4102115199437297624L;

    public ServiceRegisterException() {
        super();
    }

    public ServiceRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ServiceRegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRegisterException(String message) {
        super(message);
    }

    public ServiceRegisterException(Throwable cause) {
        super(cause);
    }

}
