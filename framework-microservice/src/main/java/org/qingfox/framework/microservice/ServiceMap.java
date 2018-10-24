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
package org.qingfox.framework.microservice;

import java.io.InputStream;

import org.jboss.resteasy.specimpl.MultivaluedMapImpl;

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
public class ServiceMap<K, V> extends MultivaluedMapImpl<K, V> {

	private static final long serialVersionUID = -6418904446897171467L;

	private InputStream bodyStream;

	public ServiceMap() {
		super();
	}

	public ServiceMap(MultivaluedMapImpl<K, V> multivaluedMap, InputStream bodyStream) {
		addAll(multivaluedMap);
		this.bodyStream = bodyStream;
	}

	/**
	 * @return bodyStream属性
	 */
	public InputStream getBodyStream() {
		return bodyStream;
	}

	/**
	 * @param bodyStream
	 *            设置bodyStream属性
	 */
	public void setBodyStream(InputStream bodyStream) {
		this.bodyStream = bodyStream;
	}

}
