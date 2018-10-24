package com.framework.plugin.web.system.bean;

import java.io.Serializable;
import java.util.List;

public class DictBean implements Serializable {
    private static final long serialVersionUID = 1748618075367273791L;

    private String code;
    private String text;
    private String value;
    private List<DictBean> list;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DictBean> getList() {
        return list;
    }

    public void setList(List<DictBean> list) {
        this.list = list;
    }

}
