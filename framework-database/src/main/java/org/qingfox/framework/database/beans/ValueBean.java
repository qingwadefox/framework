package org.qingfox.framework.database.beans;

import java.io.Serializable;
import java.util.Date;

import org.qingfox.framework.common.utils.ConvertUtil;

public class ValueBean implements Serializable {

    private static final long serialVersionUID = 948673096262407990L;

    private Object value;

    public ValueBean() {

    }

    public ValueBean(Object value) {
        this.value = value;
    }

    /**
     * @return value属性
     */
    public Object get() {
        return value;
    }

    /**
     * @param value 设置value属性
     */
    public void set(Object value) {
        this.value = value;
    }

    public String getString() {
        return ConvertUtil.convert(value, String.class);
    }

    public Integer getInt() {
        return ConvertUtil.convert(value, Integer.class);
    }

    public Long getLong() {
        return ConvertUtil.convert(value, Long.class);
    }

    public Boolean getBoolean() {
        return ConvertUtil.convert(value, Boolean.class);
    }

    public Date getDate() {
        return ConvertUtil.convert(value, Date.class);
    }

}
