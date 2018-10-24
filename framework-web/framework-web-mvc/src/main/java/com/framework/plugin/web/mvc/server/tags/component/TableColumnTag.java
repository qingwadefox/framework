package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentDataTag;

public class TableColumnTag extends BasicComponentDataTag {

    private static final long serialVersionUID = -2952889960115557744L;

    private String id;
    private String name;
    private String sort;
    private String filter;
    private String hide;
    private String render;
    private String dictcode;

    @Override
    public Object getTagObject() {
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public String getRender() {
        return render;
    }

    public void setRender(String render) {
        this.render = render;
    }

    public String getDictcode() {
        return dictcode;
    }

    public void setDictcode(String dictcode) {
        this.dictcode = dictcode;
    }

}
