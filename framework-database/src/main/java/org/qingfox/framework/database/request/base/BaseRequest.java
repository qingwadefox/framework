package org.qingfox.framework.database.request.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.qingfox.framework.database.beans.ConditionBean;
import org.qingfox.framework.database.beans.PageBean;

public class BaseRequest<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer top;
	private List<ConditionBean> conditions;
	private List<String> fields;
	private PageBean<T> page;
	private String table;

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

	public PageBean<T> getPage() {
		return page;
	}

	public void setPage(PageBean<T> page) {
		this.page = page;
	}

	public List<ConditionBean> getConditions() {
		return conditions;
	}

	public void setConditions(List<ConditionBean> conditions) {
		this.conditions = conditions;
	}

	public void addCondition(ConditionBean condition) {
		if (this.conditions == null) {
			this.conditions = new ArrayList<ConditionBean>();
		}
		this.conditions.add(condition);
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void addField(String... fields) {
		if (this.fields == null) {
			this.fields = new ArrayList<String>();
		}
		for (String field : fields) {
			this.fields.add(field);
		}
	}

}
