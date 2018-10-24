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
package com.framework.microservice.test.resources;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.microservice.ServiceMap;
import com.framework.microservice.ServiceResult;
import com.framework.microservice.annotations.Register;
import com.framework.microservice.annotations.Service;
import com.framework.microservice.context.ServiceResponse;
import com.framework.microservice.test.entity.TestEntity;

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
@Register("test2")
public class Test2Resource {
	private static final ILogger logger = LoggerFactory.getLogger(Test2Resource.class);

	@Service(value = "test", name = "服务名称", params = {"param1", "param2"}, paramsdetail = {"参数1", "参数2"}, returndetail = "返回说明")
	public ServiceResult<TestEntity> test(ServiceMap<String, String> map, ServiceResponse response) {
		ServiceResult<TestEntity> result = new ServiceResult<TestEntity>();
		TestEntity entity = new TestEntity();
		entity.setP1(123);
		entity.setP2("我是中文 谢谢");
		result.setResult(entity);
		logger.debug("测试日志【", JSONObject.toJSONString(result), "】");
		return result;
	}
}
