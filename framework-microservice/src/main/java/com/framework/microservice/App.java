package com.framework.microservice;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.microservice.context.Constants;

/**
 * 
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年4月28日
 * @功能说明：
 *
 */
public class App {
	private static final ILogger logger = LoggerFactory.getLogger(App.class);
	private static MicroserviceServer server;

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				if (server != null) {
					server.stop();
				}
			}
		});

		String microservicePortEnv = System.getenv(Constants.ENV_SERVICE_PORT);
		Integer microservicePort = 8080;
		if (StringUtils.isNotEmpty(microservicePortEnv)) {
			microservicePort = Integer.parseInt(microservicePortEnv);
		}
		logger.info("服务端口为【", microservicePort, "】");

		try {
			logger.info("开始启动服务...");
			server = new MicroserviceServer(microservicePort);
			server.start();
			logger.info("启动服务成功...");
		} catch (Exception e) {
			logger.error(e, "启动服务失败...");
		}

	}
}
