package org.qingfox.framework.test.pdd.entity;

import java.io.Serializable;

public class CatEntity implements Serializable {

	private static final long serialVersionUID = 6840561957042541237L;

	private String id;
	private String name;
	private String pid;
	private String lv;
	private String rootId;

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

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getLv() {
		return lv;
	}

	public void setLv(String lv) {
		this.lv = lv;
	}

	public String getRootId() {
		return rootId;
	}

	public void setRootId(String rootId) {
		this.rootId = rootId;
	}

}
