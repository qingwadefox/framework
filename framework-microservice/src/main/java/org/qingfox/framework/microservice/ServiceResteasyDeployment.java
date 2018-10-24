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
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.qingfox.framework.microservice.annotations.Register;
import org.qingfox.framework.microservice.annotations.Service;
import org.qingfox.framework.microservice.bean.ServerBean;
import org.qingfox.framework.microservice.context.Constants;
import org.qingfox.framework.microservice.factory.MicroserviceFactory;
import org.qingfox.framework.microservice.resource.APIResource;
import org.qingfox.framework.microservice.resource.ServiceResource;
import org.qingfox.framework.microservice.resource.StatisticsResource;
import org.qingfox.framework.microservice.utils.ServiceUtil;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.scanner.PackageScanner;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年3月17日
 * @功能说明：
 * 
 */
public class ServiceResteasyDeployment extends ResteasyDeployment {

	private static final ILogger logger = LoggerFactory.getLogger(ServiceResteasyDeployment.class);

	private String scanPackage;

	public ServiceResteasyDeployment() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.jboss.resteasy.spi.ResteasyDeployment#setProviderClasses(java.util.List)
	 * @author zhengwei 2017年4月13日 zhengwei
	 */
	@Override
	public void setProviderClasses(List<String> providerClasses) {
		super.setProviderClasses(providerClasses);
	}

	@Override
	public void registration() {
		super.registration();

		ServerBean serverBean = MicroserviceFactory.getServiceBean();

		// 扫描资源类
		if (StringUtils.isNotEmpty(scanPackage)) {
			logger.info("开始扫描包【", scanPackage, "】...");
			PackageScanner scanner = new PackageScanner(scanPackage);
			try {
				List<String> list = scanner.scan();
				logger.info("扫描包中类总数为【", list.size(), "】...");
				for (String classStr : list) {
					// System.out.println(classStr);
					Class<?> _class = null;
					try {
						_class = Class.forName(classStr, false, this.getClass().getClassLoader());
					} catch (ExceptionInInitializerError | ClassNotFoundException | UnsatisfiedLinkError | NoClassDefFoundError e) {
						e.printStackTrace();
						logger.warn("获取类【", classStr, "】失败：", e.getMessage());
						continue;
					}
					Register register = _class.getAnnotation(Register.class);
					if (register != null) {
						logger.info("开始注册类【", classStr, "】...");
						String path = Constants.PREFIX_SERVICE + "/" + _class.getName().replace(".", "/");
						try {
							ServiceResource serviceResource = new ServiceResource(_class);
							registry.addSingletonResource(serviceResource, path);
							logger.info("访问路径为【", path, "】");
							List<String> annotationsPathList = serviceResource.getAnnotationsPathList();
							if (annotationsPathList != null && !annotationsPathList.isEmpty()) {
								for (String annotationsPath : annotationsPathList) {
									registry.addSingletonResource(serviceResource, Constants.PREFIX_SERVICE + annotationsPath);
									logger.info("访问路径为【", Constants.PREFIX_SERVICE + annotationsPath, "】");
								}
							}

							for (Method method : _class.getMethods()) {
								Service service = method.getAnnotation(Service.class);
								String registerValue = StringUtils.isEmpty(register.value()) ? null : register.value();
								if (service != null) {
									String[] paths = null;
									if (StringUtils.isNotEmpty(registerValue) && StringUtils.isNotEmpty(service.value())) {
										paths = ArrayUtils.add(paths, Constants.PREFIX_SERVICE + ServiceUtil.getAnnotationsPath(registerValue, service.value()));
									}
									paths = ArrayUtils.add(paths, path + "/" + method.getName());
									serverBean.addService(classStr + "." + method.getName(), service.name(), paths);
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				logger.error(e, "扫描类失败.");
			}
		}
		registry.addSingletonResource(new APIResource());
		registry.addSingletonResource(new StatisticsResource());
	}
	/**
	 * @return scanPackage属性
	 */
	public String getScanPackage() {
		return scanPackage;
	}

	/**
	 * @param scanPackage
	 *            设置scanPackage属性
	 */
	public void setScanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
	}

}
