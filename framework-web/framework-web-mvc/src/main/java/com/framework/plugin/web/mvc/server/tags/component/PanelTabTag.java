package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentDataTag;

public class PanelTabTag extends BasicComponentDataTag {

    private static final long serialVersionUID = -132225294101533359L;
    private String name;
    private String id;
    private String active;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

}
