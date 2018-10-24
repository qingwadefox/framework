package com.framework.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

public class ListUtil {

    /**
     * 将数组转化为list
     * 
     * @param objs
     * @return
     */
    public static <T> List<T> toList(T[] objs) {
        if (objs == null) {
            return null;
        }
        List<T> list = new ArrayList<T>();
        for (T obj : objs) {
            list.add(obj);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> reverse(List<T> list) {
        Object[] objs = list.toArray();
        ArrayUtils.reverse(objs);
        return (List<T>) toList(objs);
    }

    /**
     * 将数据格式化为树形列表
     * 
     * @param list
     * @param idField
     * @param pidField
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static List<Map<String, Object>> toTreeList(
            List<Map<String, Object>> list, String idField, String pidField,
            Object firstId, String childField) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> obj : list) {
            Object id = ReflectUtil.getFieldValue(obj, idField);
            Object pid = ReflectUtil.getFieldValue(obj, pidField);

            if (pid.equals(firstId)) {
                resultList.add(obj);
                List<Map<String, Object>> childList = toTreeList(list, idField,
                        pidField, id, childField);
                if (childList.size() > 0) {
                    ReflectUtil.setFieldValue(obj, childField, childList);
                }
            }

        }

        return resultList;

    }

    /**
     * 查找某个字段的列表
     * 
     * @param list
     * @param childField
     * @param field
     * @param value
     * @return
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> findTreeList(List<T> list, String childField,
            String field, Object value) throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        List<T> resultList = new ArrayList<T>();
        for (T obj : list) {
            Object _value = ReflectUtil.getFieldValue(obj, field);
            if (_value.equals(value)) {
                resultList.add(obj);
            }
            List<T> childList = (List<T>) ReflectUtil.getFieldValue(obj,
                    childField);
            if (childList != null && childList.size() > 0) {
                resultList.addAll(findTreeList(childList, childField, field,
                        value));
            }
        }
        return resultList;

    }
}
