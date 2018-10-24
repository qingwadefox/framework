package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentDataTag;

public class ModalButtonTag extends BasicComponentDataTag {

    private static final long serialVersionUID = -1724409356448659967L;

    private String name;
    private String onclick;

    @Override
    public Object getTagObject() {
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }

}
