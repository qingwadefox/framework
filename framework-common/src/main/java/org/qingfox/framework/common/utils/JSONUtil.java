package org.qingfox.framework.common.utils;

import java.lang.reflect.Field;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JSONUtil {

    public static JSONArray toJSONArray(List<?> list) {
        if (list == null || list.isEmpty()) {
            return null;
        } else {
            JSONArray jsonArray = new JSONArray();
            for (Object object : list) {
                jsonArray.add(JSONObject.toJSON(object));
            }
            return jsonArray;
        }
    }

    public static JSONObject toJSONObject(Object object) {
        JSONObject jsonObject = new JSONObject();

        for (Field field : object.getClass().getDeclaredFields()) {
            Object value = ReflectUtil.getFieldValueByGetMethod(object, field);
            if (value != null) {
                jsonObject.put(field.getName(), value);
            }
        }
        return jsonObject;
    }

}
