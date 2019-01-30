package org.qingfox.framework.microservice.context;

import java.io.Serializable;
import java.util.Map;

import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.utils.ConvertUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ServiceRequest implements Serializable {

    private static final ILogger logger = LoggerFactory.getLogger(ServiceRequest.class);

    private static final long serialVersionUID = 2216421646730330648L;

    private JSONObject urlParamsJSON;
    private JSONObject bodyParamsJSON;
    private String bodyString;

    public ServiceRequest(Map<String, String[]> urlParameterMap, String bodyString) {
        urlParamsJSON = (JSONObject) JSON.toJSON(urlParameterMap);
        logger.debug(" params json - ", urlParamsJSON.toJSONString());
        try {
            bodyParamsJSON = JSON.parseObject(bodyString);
            logger.debug(" body json - ", bodyParamsJSON.toJSONString());
        } catch (Exception e) {
            this.bodyString = bodyString;
            logger.debug(" body string - ", bodyString);
        }
    }

    public String getURLString(String name) {
        return this.getURLString(name, 0);
    }

    public String getURLString(String name, Integer index) {
        if (urlParamsJSON == null) {
            return null;
        }
        JSONArray jsonArray = urlParamsJSON.getJSONArray(name);
        if (jsonArray != null && jsonArray.size() > index) {
            return jsonArray.getString(index);
        } else {
            return null;
        }
    }

    public <T> T getURLObject(String name, Class<T> clazz) {
        return ConvertUtil.convert(getURLString(name, 0), clazz);
    }

    public <T> T getURLObject(String name, Integer index, Class<T> clazz) {
        return ConvertUtil.convert(getURLString(name, index), clazz);
    }

    public String[] getURLStrings(String name) {
        if (urlParamsJSON == null) {
            return null;
        }
        JSONArray jsonArray = urlParamsJSON.getJSONArray(name);
        if (jsonArray != null && !jsonArray.isEmpty()) {
            String[] params = new String[jsonArray.size()];
            return jsonArray.toArray(params);
        } else {
            return null;
        }
    }

    public String getBodyString(String name) {
        return this.getBodyObject(name, String.class);
    }

    public <T> T getBodyObject(String name, Class<T> clazz) {
        if (bodyParamsJSON == null) {
            return null;
        }
        Object obj = bodyParamsJSON.get(name);
        return ConvertUtil.convert(obj, clazz);
    }

    public String getBodyString() {
        return bodyString;
    }

}
