package com.framework.plugin.web.common.server.response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.server.response.basic.BasicResponse;

public class ServiceResponse<T> extends BasicResponse {
    private static final long serialVersionUID = -5394807226786203599L;

    private T result;

    public T getResult() {
        return result;
    }

    public ServiceResponse<T> setResult(T result) {
        this.result = result;
        return this;
    }

    public void print(HttpServletResponse response) {
        this.print(response, null);
    }

    public void print(HttpServletResponse response, String contentType) {

        if (StringUtils.isEmpty(contentType)) {
            contentType = "application/json; charset=utf-8";
        }
        try {
            switch (this.getMode()) {
                case 1:
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(contentType);
                    response.getWriter().write(JSONObject.toJSONString(this));
                    break;
                case 2:
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType(contentType);
                    OutputStream outputStream = response.getOutputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(this);
                    outputStream.write(baos.toByteArray());
                    outputStream.flush();
                    outputStream.close();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
