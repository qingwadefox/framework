package org.qingfox.framework.database.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.qingfox.framework.database.enums.ConditionEnum;

public class ConditionBean implements Serializable {
	private static final long serialVersionUID = -3586617801288109585L;
	private String name;
	private ConditionEnum condition = ConditionEnum.EQ;
	private String treeId;
	private String treePid;
	private List<Object> values;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getTreePid() {
		return treePid;
	}

	public void setTreePid(String treePid) {
		this.treePid = treePid;
	}

	public ConditionEnum getCondition() {
		return condition;
	}

	public void setCondition(ConditionEnum condition) {
		this.condition = condition;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	public void addValue(Object value) {
		if (this.values == null) {
			this.values = new ArrayList<Object>();
		}
		this.values.add(value);
	}

}
