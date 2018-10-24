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
package com.framework.microservice.context;

import com.alibaba.fastjson.JSONObject;
import com.framework.microservice.MicroserviceClient;
import com.framework.microservice.ServiceMap;
import com.framework.microservice.ServiceResult;

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

// 192.168.1.102
// 255.255.255.0
// 192.168.1.1
// 355877064827431

public class ServiceResponse {
	private String serviceId;
	private int serviceStep = 0;

	@SuppressWarnings("unchecked")
	public <T> ServiceResult<T> sendURL(String url, ServiceMap<String, Object> param) {
		ServiceMap<String, Object> headerParam = new ServiceMap<String, Object>();
		headerParam.putSingle("SERVICE-ID", serviceId);
		headerParam.putSingle("SERVICE-STEP", serviceStep);
		String content = MicroserviceClient.post(url, new ServiceMap<String, String>(), headerParam);
		return JSONObject.parseObject(content, ServiceResult.class);
	}

	@SuppressWarnings("unchecked")
	public <T> ServiceResult<T> sendURL(String url) {
		ServiceMap<String, Object> headerParam = new ServiceMap<String, Object>();
		headerParam.putSingle("SERVICE-ID", serviceId);
		headerParam.putSingle("SERVICE-STEP", serviceStep);
		String content = MicroserviceClient.post(url, new ServiceMap<String, String>(), headerParam);
		return JSONObject.parseObject(content, ServiceResult.class);
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setServiceStep(int serviceStep) {
		this.serviceStep = serviceStep;
	}

}
