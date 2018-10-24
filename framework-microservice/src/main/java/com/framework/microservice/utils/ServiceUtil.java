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
package com.framework.microservice.utils;

import org.jboss.netty.channel.ChannelHandlerContext;

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
public class ServiceUtil {

	/**
	 * 拼接注解路径 .
	 * 
	 * @param registerPath
	 * @param servicePath
	 * @return
	 * @author zhengwei 2017年4月13日 zhengwei
	 */
	public static String getAnnotationsPath(String registerPath, String servicePath) {
		return (registerPath.startsWith("/") ? registerPath : "/" + registerPath) + (servicePath.startsWith("/") ? servicePath : "/" + servicePath);
	}
	public static String getIp(ChannelHandlerContext ctx) {
		String ip = ctx.getChannel().getRemoteAddress().toString();
		ip = ip.substring(ip.indexOf("/") + 1, ip.indexOf(":"));
		return ip;
	}
}
