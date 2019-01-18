package org.qingfox.framework.microservice.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qingfox.framework.common.utils.ReflectUtil;

public class ServiceRequest implements Serializable {

    private static final long serialVersionUID = 2216421646730330648L;

    private Map<String, Object> urlParams;
    private Map<String, Object> bodyParams;

    public static ServiceRequest newInstance() {
        return new ServiceRequest();
    }

    public ServiceRequest() {
        urlParams = new HashMap<>();
        bodyParams = new HashMap<>();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ServiceRequest addUrlParams(String name, Object value) {
        Object _value = urlParams.get(name);
        if (_value == null) {
            urlParams.put(name, value);
        } else if (ReflectUtil.existInterfaces(_value.getClass(), Collection.class)) {
            ((Collection) _value).add(value);
            urlParams.put(name, _value);
        } else {
            List<Object> list = new ArrayList<>();
            list.add(_value);
            list.add(value);
            urlParams.put(name, list);
        }
        return this;
    }

    public ServiceRequest addAndReplaceUrlParams(String name, Object value) {
        urlParams.put(name, value);
        return this;
    }

    public ServiceRequest addAndReplaceBodyParams(String name, Object value) {
        bodyParams.put(name, value);
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ServiceRequest addBodyParams(String name, Object value) {
        Object _value = bodyParams.get(name);
        if (_value == null) {
            bodyParams.put(name, value);
        } else if (ReflectUtil.existInterfaces(_value.getClass(), Collection.class)) {
            ((Collection) _value).add(value);
            bodyParams.put(name, _value);
        } else {
            List<Object> list = new ArrayList<>();
            list.add(_value);
            list.add(value);
            bodyParams.put(name, list);
        }
        return this;
    }

    public Map<String, Object> getUrlParams() {
        return urlParams;
    }

    public ServiceRequest setUrlParams(Map<String, Object> urlParams) {
        this.urlParams = urlParams;
        return this;
    }

    public Map<String, Object> getBodyParams() {
        return bodyParams;
    }

    public ServiceRequest setBodyParams(Map<String, Object> bodyParams) {
        this.bodyParams = bodyParams;
        return this;
    }

}
