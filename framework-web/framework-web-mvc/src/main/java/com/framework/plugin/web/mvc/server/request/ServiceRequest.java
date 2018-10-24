package com.framework.plugin.web.common.server.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.server.request.basic.BasicRequest;

public class ServiceRequest extends BasicRequest implements Serializable {

    private static final long serialVersionUID = 8635959101373278384L;

    private String appCode;
    private String appFunction;

    private Map<String, Object> parameter;

    public ServiceRequest putParameter(String key, Object value) {
        if (parameter == null) {
            parameter = new HashMap<String, Object>();
        }
        parameter.put(key, value);
        return this;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public ServiceRequest setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
        return this;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppFunction() {
        return appFunction;
    }

    public void setAppFunction(String appFunction) {
        this.appFunction = appFunction;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("appCode", appCode);
        json.put("appFunction", appFunction);
        return json.toJSONString();
    }

}
