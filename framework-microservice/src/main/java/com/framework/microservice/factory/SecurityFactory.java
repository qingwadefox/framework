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
package com.framework.microservice.factory;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年5月26日
 * @功能说明：
 * 
 */
public class SecurityFactory {
	private static final ILogger logger = LoggerFactory.getLogger(SecurityFactory.class);

	private static boolean security = false;
	private static JSONObject securityObject;

	public static void setSecurity(boolean security) {
		SecurityFactory.security = security;
	}

	/**
	 * 初始化权限信息 .
	 * 
	 * @param address
	 * @throws Exception
	 * @author zhengwei 2017年5月26日 zhengwei
	 */
	public static void init(String address) throws Exception {
		securityObject = new JSONObject();
		refresh();
	}

	/**
	 * 刷新权限 .
	 * 
	 * @throws Exception
	 * @author zhengwei 2017年5月26日 zhengwei
	 */
	public static void refresh() throws Exception {

	}

	/**
	 * 验证方法路径权限 .
	 * 
	 * @param ip
	 * @param methodPath
	 * @return
	 * @author zhengwei 2017年5月26日 zhengwei
	 */
	public static boolean checkSecurity(String ip, String methodPath) {

		if (!security) {
			return true;
		}

		if (securityObject == null) {
			return false;
		}
		JSONArray securityArray = securityObject.getJSONArray(ip);
		if (securityArray == null || securityArray.isEmpty()) {
			return false;
		}
		if (securityArray.contains(methodPath)) {
			return true;
		} else {
			return false;
		}
	}

}
