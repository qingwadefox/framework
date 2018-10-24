/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.framework.microservice.test.entity;

import java.io.Serializable;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年3月20日
 * @功能说明：
 * 
 */
public class TestEntity implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 5656450392679224421L;
	private Integer p1;
	private String p2;
	/**
	 * @return p1属性
	 */
	public Integer getP1() {
		return p1;
	}
	/**
	 * @param p1
	 *            设置p1属性
	 */
	public void setP1(Integer p1) {
		this.p1 = p1;
	}
	/**
	 * @return p2属性
	 */
	public String getP2() {
		return p2;
	}
	/**
	 * @param p2
	 *            设置p2属性
	 */
	public void setP2(String p2) {
		this.p2 = p2;
	}

}
