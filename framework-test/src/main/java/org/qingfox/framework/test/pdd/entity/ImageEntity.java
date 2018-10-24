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
package org.qingfox.framework.test.pdd.entity;

import java.io.Serializable;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年12月19日
 * @功能说明：
 * 
 */
public class ImageEntity implements Serializable {

	private static final long serialVersionUID = -6656988602875196239L;

	private String url;
	private Integer width;
	private Integer height;
	/**
	 * @return url属性
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url
	 *            设置url属性
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return width属性
	 */
	public Integer getWidth() {
		return width;
	}
	/**
	 * @param width
	 *            设置width属性
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	/**
	 * @return height属性
	 */
	public Integer getHeight() {
		return height;
	}
	/**
	 * @param height
	 *            设置height属性
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

}
