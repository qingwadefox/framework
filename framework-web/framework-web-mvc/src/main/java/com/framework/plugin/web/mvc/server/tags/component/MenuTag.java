package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentTag;

public class MenuTag extends BasicComponentTag {

    private static final long serialVersionUID = -6721759030935857494L;
    private String id;
    private String appCode;
    private String appFunction;
    private String sidebarClick;
    private String forId;
    private String idField;
    private String pidField;
    private String textFiled;
    private String iconField;
    private String urlField;

    @Override
    public String getType() {
        return "menu";
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

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public String getPidField() {
        return pidField;
    }

    public void setPidField(String pidField) {
        this.pidField = pidField;
    }

    public String getTextFiled() {
        return textFiled;
    }

    public void setTextFiled(String textFiled) {
        this.textFiled = textFiled;
    }

    public String getIconField() {
        return iconField;
    }

    public void setIconField(String iconField) {
        this.iconField = iconField;
    }

    public String getUrlField() {
        return urlField;
    }

    public void setUrlField(String urlField) {
        this.urlField = urlField;
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

    public String getSidebarClick() {
        return sidebarClick;
    }

    public void setSidebarClick(String sidebarClick) {
        this.sidebarClick = sidebarClick;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }

}
