package com.framework.plugin.web.common.server.tags.component;

import com.framework.plugin.web.common.server.tags.basic.BasicComponentTag;

public class TreeTag extends BasicComponentTag {
    private static final long serialVersionUID = 5473561800670177939L;

    private String id;
    private String tableId;
    private String rootId;
    private String rootName;
    private String appCode;
    private String appFunction;
    private String idField;
    private String pidField;
    private String textFiled;
    private String iconField;
    private String contextmenuFn;
    private String init;
    private String selection;

    @Override
    public String getType() {
        return "tree";
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

    public String getContextmenuFn() {
        return contextmenuFn;
    }

    public void setContextmenuFn(String contextmenuFn) {
        this.contextmenuFn = contextmenuFn;
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getRootId() {
        return rootId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
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
