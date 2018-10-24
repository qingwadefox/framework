/**
 * 
 */
package com.framework.test.pdd.entity;

import java.io.Serializable;

/**
 * @author zhengwei
 *
 */
public class ExpressEntity implements Serializable {

	private static final long serialVersionUID = -9087879272751341151L;

	private String id;
	private String name;

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

}
