package com.framework.plugin.web.common.server.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.server.response.basic.BasicResponse;

public class WebSocketResponse<T> extends BasicResponse {

    private static final long serialVersionUID = 3241330076567810255L;

    private String cmd;

    private T result;

    public T getResult() {
        return result;
    }

    public WebSocketResponse<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public String getCmd() {
        return cmd;
    }

    public WebSocketResponse<T> setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public void print(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage(this.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
