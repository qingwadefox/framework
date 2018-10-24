package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentTag;

public class TableTag extends BasicComponentTag {

    private static final long serialVersionUID = 1485623752354271119L;
    private String id;
    private String tableId;
    private String appCode;
    private String appFunction;
    private String select;
    private String init;
    private String pagination;

    @Override
    public String getType() {
        return "table";
    }

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

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getPagination() {
        return pagination;
    }

    public void setPagination(String pagination) {
        this.pagination = pagination;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppFunction() {
        return appFunction;
    }

    public void setAppFunction(String appFunction) {
        this.appFunction = appFunction;
    }

}
