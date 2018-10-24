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
package com.framework.microservice.providers;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.alibaba.fastjson.JSONObject;
import com.framework.microservice.ServiceResult;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年4月13日
 * @功能说明：
 * 
 */
public class ExceptionProvider implements ExceptionMapper<Exception> {

	/**
	 * {@inheritDoc}
	 * 
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 * @author zhengwei 2017年4月13日 zhengwei
	 */
	@Override
	public Response toResponse(Exception exception) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		if (exception.getClass().equals(javax.ws.rs.NotFoundException.class)) {
			result.setErrorCode(404);
			result.setErrorMsg("page not found");
		} else {
			exception.printStackTrace();
			result.setErrorMsg(getStackTraceAsString(exception));
			result.setErrorCode(1);
		}
		return Response.status(Status.OK).entity(JSONObject.toJSONString(result)).build();
	}
	private String getStackTraceAsString(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		throwable.printStackTrace(printWriter);
		StringBuffer error = stringWriter.getBuffer();
		return error.toString();
	}

}
