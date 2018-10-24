package com.framework.plugin.web.common.server.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.framework.common.beans.ConditionBean;
import com.framework.common.beans.PageBean;
import com.framework.plugin.web.common.server.request.basic.BasicRequest;
import com.framework.plugin.web.common.server.utils.DataUtil;

public class CRUDRequest extends BasicRequest implements Serializable {

    private static final long serialVersionUID = 6113644631421330678L;
    private String table;
    private List<ConditionBean> conditions;
    private List<LinkedHashMap<String, String>> datas;
    private Integer top;
    private String[] fields;
    private PageBean page;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public List<ConditionBean> getConditions() {
        return conditions;
    }

    public void setConditions(List<ConditionBean> conditions) {
        this.conditions = conditions;
    }

    public void setCondition(ConditionBean condition) {
        List<ConditionBean> conditions = new ArrayList<ConditionBean>();
        conditions.add(condition);
        this.conditions = conditions;
    }

    public void addField(String field) {
        fields = ArrayUtils.add(fields, field);
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public List<LinkedHashMap<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<LinkedHashMap<String, String>> datas) {
        for (LinkedHashMap<String, String> data : datas) {
            DataUtil.dataEscape(data);
        }
        this.datas = datas;
    }

    public void addData(LinkedHashMap<String, String> data) {
        if (this.getDatas() == null) {
            DataUtil.dataEscape(data);
            this.datas = new ArrayList<LinkedHashMap<String, String>>();
        }
        this.datas.add(data);
    }

    public void addData(Map<String, String> data) {
        LinkedHashMap<String, String> _data = new LinkedHashMap<String, String>();
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            _data.put(entry.getKey(), entry.getValue());
        }
        this.addData(_data);
    }

    public void addCondition(ConditionBean bean) {
        if (bean != null) {
            if (this.conditions == null) {
                this.conditions = new ArrayList<ConditionBean>();
            }
            this.conditions.add(bean);
        }
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

}
