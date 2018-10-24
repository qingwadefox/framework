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
 * @创建日期：2017年12月18日
 * @功能说明：
 * 
 */
public class SimpleSkuEntity implements Serializable {
	private static final long serialVersionUID = -8671987961060336454L;
	private String taobaoSkuId;
	private String taobaoSkuImgUrl;
	private String taobaoSkuParentText;
	private String taobaoSkuText;
	/**
	 * @return taobaoSkuId属性
	 */
	public String getTaobaoSkuId() {
		return taobaoSkuId;
	}
	/**
	 * @param taobaoSkuId
	 *            设置taobaoSkuId属性
	 */
	public void setTaobaoSkuId(String taobaoSkuId) {
		this.taobaoSkuId = taobaoSkuId;
	}
	/**
	 * @return taobaoSkuText属性
	 */
	public String getTaobaoSkuText() {
		return taobaoSkuText;
	}
	/**
	 * @param taobaoSkuText
	 *            设置taobaoSkuText属性
	 */
	public void setTaobaoSkuText(String taobaoSkuText) {
		this.taobaoSkuText = taobaoSkuText;
	}
	/**
	 * @return taobaoSkuImgUrl属性
	 */
	public String getTaobaoSkuImgUrl() {
		return taobaoSkuImgUrl;
	}
	/**
	 * @param taobaoSkuImgUrl
	 *            设置taobaoSkuImgUrl属性
	 */
	public void setTaobaoSkuImgUrl(String taobaoSkuImgUrl) {
		this.taobaoSkuImgUrl = taobaoSkuImgUrl;
	}
	/**
	 * @return taobaoSkuParentText属性
	 */
	public String getTaobaoSkuParentText() {
		return taobaoSkuParentText;
	}
	/**
	 * @param taobaoSkuParentText
	 *            设置taobaoSkuParentText属性
	 */
	public void setTaobaoSkuParentText(String taobaoSkuParentText) {
		this.taobaoSkuParentText = taobaoSkuParentText;
	}

}
