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
package com.framework.microservice;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

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
public class MicroserviceClient {

	public static String get(String url, Map<String, String> queryParam, ServiceMap<String, Object> headerParam) {
		ResteasyClient client = new ResteasyClientBuilder().build();

		// 组织QUERYPARAM
		StringBuffer queryParamStr = new StringBuffer();
		if (queryParam != null && !queryParam.isEmpty()) {
			Iterator<Entry<String, String>> it = queryParam.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				queryParamStr.append(entry.getKey());
				queryParamStr.append("=");
				queryParamStr.append(entry.getValue());
				queryParamStr.append("&");
			}
		}
		if (StringUtils.isNotEmpty(queryParamStr.toString())) {
			url += "?" + queryParamStr.toString();
		}
		ResteasyWebTarget target = client.target(url);

		Builder requestBuild = target.request();
		// 组织headerparamc
		if (headerParam != null && !headerParam.isEmpty()) {
			requestBuild.headers(headerParam);
		}

		Response response = requestBuild.get();
		String content = response.readEntity(String.class);
		// ServiceResult<T> result = JSONObject.parseObject(content,
		// ServiceResult.class);
		response.close();
		client.close();
		return content;
	}
	public static String post(String url, ServiceMap<String, String> queryParam, ServiceMap<String, Object> headerParam) {
		ResteasyClient client = new ResteasyClientBuilder().build();
		ResteasyWebTarget target = client.target(url);
		Builder requestBuild = target.request();
		Entity<Form> entityForm = null;
		if (queryParam != null && queryParam.isEmpty()) {
			entityForm = Entity.form(queryParam);
		} else {
			entityForm = Entity.form(new Form());
		}

		// 组织headerparamc
		if (headerParam != null && !headerParam.isEmpty()) {
			requestBuild.headers(headerParam);
		}

		Response response = requestBuild.post(entityForm);
		String content = response.readEntity(String.class);
		System.out.println(content);
		response.close();
		client.close();
		return content;

	}
	public static void main(String[] args) {

	}
}
