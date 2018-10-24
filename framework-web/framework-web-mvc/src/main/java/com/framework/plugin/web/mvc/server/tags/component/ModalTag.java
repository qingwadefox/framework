package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentTag;

public class ModalTag extends BasicComponentTag {

    private static final long serialVersionUID = -276427295417211052L;

    private String title;
    private String id;

    @Override
    public String getType() {
        return "modal";
    }

    @Override
    public Object getTagObject() {
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
