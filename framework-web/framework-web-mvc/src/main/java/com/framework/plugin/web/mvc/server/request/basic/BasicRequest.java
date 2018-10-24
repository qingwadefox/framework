package com.framework.plugin.web.common.server.request.basic;

import java.io.Serializable;

public class BasicRequest implements Serializable {
    private static final long serialVersionUID = 2213630080688521178L;

    public static int WEB_MODE = 1;
    public static int CLIENT_MODE = 2;
    private int mode = WEB_MODE;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

}
