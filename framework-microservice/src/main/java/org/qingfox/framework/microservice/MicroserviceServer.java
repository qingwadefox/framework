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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.resteasy.plugins.server.netty.NettyJaxrsServer;
import org.qingfox.framework.microservice.bean.ServerBean;
import org.qingfox.framework.microservice.factory.MicroserviceFactory;
import org.qingfox.framework.microservice.factory.SecurityFactory;
import org.qingfox.framework.microservice.providers.ExceptionProvider;

import ch.qos.logback.core.joran.spi.JoranException;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年3月15日
 * @功能说明：
 * 
 */
public class MicroserviceServer {

	private static final ILogger logger = LoggerFactory.getLogger(MicroserviceServer.class);

	private ServerBean serverBean;

	private NettyJaxrsServer netty;

	private String scanPackage = "com";

	public MicroserviceServer(Integer port) throws Exception {
		serverBean = MicroserviceFactory.getServiceBean();
		serverBean.setServicePort(port);
		// 初始化netty服务
		try {
			logger.info("开始初始化netty服务...");
			this.initNetty();
			logger.info("初始化netty服务成功!!!");
		} catch (Exception e) {
			logger.error(e, "初始化netty服务失败!!!");
			throw e;
		}

	}

	public MicroserviceServer(Integer port, String scanPackage) throws Exception {
		this.scanPackage = scanPackage;
		serverBean = MicroserviceFactory.getServiceBean();
		serverBean.setServicePort(port);
		// 初始化netty服务
		try {
			logger.info("开始初始化netty服务...");
			this.initNetty();
			logger.info("初始化netty服务成功!!!");
		} catch (Exception e) {
			logger.error(e, "初始化netty服务失败!!!");
			throw e;
		}
	}

	private void loadSecurity() throws Exception {
		SecurityFactory.init(null);
	}

	private void initNetty() {
		netty = new NettyJaxrsServer();
		ServiceResteasyDeployment deployment = new ServiceResteasyDeployment();
		List<String> providerClasses = new ArrayList<String>();
		providerClasses.add(ExceptionProvider.class.getName());
		deployment.setProviderClasses(providerClasses);
		deployment.setScanPackage(scanPackage);
		netty.setDeployment(deployment);
		netty.setPort(serverBean.getServicePort());
		netty.setRootResourcePath("/");
		netty.setSecurityDomain(null);
	}

	public void start() throws IOException, JoranException {

		try {
			logger.info("开始启动netty服务....");
			netty.start();
			logger.info("启动netty服务成功!!!");
		} catch (Exception e) {
			logger.error(e, "启动netty服务失败!!!");
			throw e;
		}

		// 读取权限信息
		try {
			logger.info("开始读取权限信息.");
			this.loadSecurity();
			logger.info("读取权限信息成功!!!");
		} catch (Exception e) {
			logger.error(e, "读取权限信息失败!!!");
			return;
		}
	}

	public void stop() {
		logger.info("开始关闭netty服务....");
		try {
			netty.stop();
		} catch (Exception e) {
			logger.error(e, "关闭netty服务失败!!!");
		}
		logger.info("关闭netty服务成功!!!");

	}

}
