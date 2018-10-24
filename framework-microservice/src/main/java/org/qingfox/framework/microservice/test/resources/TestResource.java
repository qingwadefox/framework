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
package org.qingfox.framework.microservice.test.resources;

import org.qingfox.framework.microservice.ServiceMap;
import org.qingfox.framework.microservice.ServiceResult;
import org.qingfox.framework.microservice.annotations.Register;
import org.qingfox.framework.microservice.annotations.Service;

import com.framework.common.utils.ProcessUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年3月22日
 * @功能说明：
 * 
 */
@Register("test")
public class TestResource {

	@Service("test")
	public ServiceResult<String> test(ServiceMap<String, String> map) {
		long cmi = System.currentTimeMillis();
		ProcessUtil.getPID();
		System.out.println(System.currentTimeMillis() - cmi);
		ServiceResult<String> result = new ServiceResult<String>();
		result.setResult("Server Type : resteasy + microserviceframe");
		return result;
	}
}
