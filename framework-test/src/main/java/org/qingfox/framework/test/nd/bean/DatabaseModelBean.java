package org.qingfox.framework.test.nd.bean;

import org.qingfox.framework.database.enums.SqlClassEnum;

public class DatabaseModelBean {
    private String name;
    private SqlClassEnum type;
    private String remark;
    private String label;
    private boolean isNull;

    public DatabaseModelBean() {

    }

    public DatabaseModelBean(String name, SqlClassEnum type, String remark, String label, boolean isNull) {
        super();
        this.name = name;
        this.type = type;
        this.remark = remark;
        this.label = label;
        this.isNull = isNull;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isNull() {
        return isNull;
    }

    public void setNull(boolean isNull) {
        this.isNull = isNull;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SqlClassEnum getType() {
        return type;
    }

    public void setType(SqlClassEnum type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
