package org.qingfox.framework.database.beans;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.qingfox.framework.database.sequence.ISequenceCreate;

public class TableBean implements Serializable {
	private static final long serialVersionUID = 7460087354757064719L;

	private String name;
	private String comments;
	private FieldBean keyfield;
	private LinkedHashMap<String, FieldBean> fields;
	private Class<ISequenceCreate<?>> seqCreate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LinkedHashMap<String, FieldBean> getFields() {
		return fields;
	}

	public void setFields(LinkedHashMap<String, FieldBean> fields) {
		this.fields = fields;
	}

	public FieldBean getKeyfield() {
		return keyfield;
	}

	public void setKeyfield(FieldBean keyfield) {
		this.keyfield = keyfield;
	}

	public Class<ISequenceCreate<?>> getSeqCreate() {
		return seqCreate;
	}

	public void setSeqCreate(Class<ISequenceCreate<?>> seqCreate) {
		this.seqCreate = seqCreate;
	}

}
