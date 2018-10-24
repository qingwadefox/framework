package com.framework.plugin.web.system.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionBean implements Serializable {

    private static final long serialVersionUID = 5102894556248759323L;

    public class PmsApp implements Serializable {

        private static final long serialVersionUID = 492595807656720973L;
        public String appcode;
        public String functioncode;

        public PmsApp(String appcode, String functioncode) {
            this.appcode = appcode;
            this.functioncode = functioncode;
        }

        public String getAppcode() {
            return appcode;
        }

        public void setAppcode(String appcode) {
            this.appcode = appcode;
        }

        public String getFunctioncode() {
            return functioncode;
        }

        public void setFunctioncode(String functioncode) {
            this.functioncode = functioncode;
        }
    }

    public class PmsTable implements Serializable {

        private static final long serialVersionUID = 492595807656720973L;
        public String tablename;
        public String searchable;
        public String addable;
        public String editable;
        public String delable;

        public PmsTable(String tablename, String searchable, String addable,
                String editable, String delable) {
            this.tablename = tablename;
            this.searchable = searchable;
            this.addable = addable;
            this.editable = editable;
            this.delable = delable;
        }

        public String getTablename() {
            return tablename;
        }

        public void setTablename(String tablename) {
            this.tablename = tablename;
        }

        public String getSearchable() {
            return searchable;
        }

        public void setSearchable(String searchable) {
            this.searchable = searchable;
        }

        public String getAddable() {
            return addable;
        }

        public void setAddable(String addable) {
            this.addable = addable;
        }

        public String getEditable() {
            return editable;
        }

        public void setEditable(String editable) {
            this.editable = editable;
        }

        public String getDelable() {
            return delable;
        }

        public void setDelable(String delable) {
            this.delable = delable;
        }
    }

    private String id;
    private String code;
    private List<PmsApp> pmsAppList;
    private List<String> pmsMenuList;
    private List<PmsTable> pmsTableList;

    /**
     * 验证权限功能
     * 
     * @param appcode
     * @param functioncode
     * @return
     */
    public boolean checkPmsApp(String appcode, String functioncode) {
        boolean result = false;

        if (pmsAppList != null && !pmsAppList.isEmpty()) {
            for (PmsApp pmsApp : pmsAppList) {
                if (pmsApp.getAppcode().equals(appcode)
                        && pmsApp.getFunctioncode().equals(functioncode)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 验证表查询权限
     * 
     * @param tablename
     * @return
     */
    public boolean checkPmsTableSearchable(String tablename) {
        boolean result = false;
        for (PmsTable pmsTable : pmsTableList) {
            if (pmsTable.getTablename().equals(tablename)) {
                if (pmsTable.getSearchable() != null
                        && pmsTable.getSearchable().equals("1")) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    /**
     * 验证表新增权限
     * 
     * @param tablename
     * @return
     */
    public boolean checkPmsTableAddable(String tablename) {
        boolean result = false;
        for (PmsTable pmsTable : pmsTableList) {
            if (pmsTable.getTablename().equals(tablename)) {
                if (pmsTable.getAddable() != null
                        && pmsTable.getAddable().equals("1")) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    /**
     * 验证表编辑权限
     * 
     * @param tablename
     * @return
     */
    public boolean checkPmsTableEditable(String tablename) {
        boolean result = false;
        for (PmsTable pmsTable : pmsTableList) {
            if (pmsTable.getTablename().equals(tablename)) {
                if (pmsTable.getEditable() != null
                        && pmsTable.getEditable().equals("1")) {
                    result = true;
                }
                break;
            }
        }
        return result;
    }

    /**
     * 验证表删除权限
     * 
     * @param tablename
     * @return
     */
    public boolean checkPmsTableDelable(String tablename) {
        boolean result = false;
        for (PmsTable pmsTable : pmsTableList) {
            if (pmsTable.getTablename().equals(tablename)) {
                if (pmsTable.getDelable() != null
                        && pmsTable.getDelable().equals("1")) {
                    result = true;
                    break;
                }
                break;
            }
        }
        return result;
    }

    public List<PmsApp> getPmsAppList() {
        return pmsAppList;
    }

    public void setPmsAppList(List<PmsApp> pmsAppList) {
        this.pmsAppList = pmsAppList;
    }

    public List<String> getPmsMenuList() {
        return pmsMenuList;
    }

    public void setPmsMenuList(List<String> pmsMenuList) {
        this.pmsMenuList = pmsMenuList;
    }

    public List<PmsTable> getPmsTableList() {
        return pmsTableList;
    }

    public void setPmsTableList(List<PmsTable> pmsTableList) {
        this.pmsTableList = pmsTableList;
    }

    public void addPmsApp(String appcode, String functioncode) {
        if (pmsAppList == null) {
            pmsAppList = new ArrayList<PmsApp>();
        }
        pmsAppList.add(new PmsApp(appcode, functioncode));
    }

    public void addPmsMenu(String menuId) {
        if (pmsMenuList == null) {
            pmsMenuList = new ArrayList<String>();
        }
        pmsMenuList.add(menuId);
    }

    public void addPmsTable(String tablename, String searchable,
            String addable, String editable, String delable) {
        if (pmsTableList == null) {
            pmsTableList = new ArrayList<PmsTable>();
        }
        pmsTableList.add(new PmsTable(tablename, searchable, addable, editable,
                delable));

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
