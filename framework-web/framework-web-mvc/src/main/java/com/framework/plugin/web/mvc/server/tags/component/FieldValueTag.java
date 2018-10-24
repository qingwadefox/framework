package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentDataTag;

public class FieldValueTag extends BasicComponentDataTag {

    private static final long serialVersionUID = -1123108600527854069L;

    private String value;
    private String name;

    @Override
    public Object getTagObject() {
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
