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
package com.framework.microservice.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年5月10日
 * @功能说明：
 * 
 */
public class ServerBean implements Serializable {

	private static final long serialVersionUID = -6635125437192866152L;

	private String sysDir;
	private int servicePort;
	private String podName;

	private List<ServiceBean> serviceList;

	public void addService(String packagePath, String name, String[] paths) {
		if (serviceList == null) {
			serviceList = new ArrayList<ServiceBean>();
		}
		serviceList.add(new ServiceBean(packagePath, name, paths));
	}

	/**
	 * @return serviceList属性
	 */
	public List<ServiceBean> getServiceList() {
		return serviceList;
	}

	/**
	 * @return sysDir属性
	 */
	public String getSysDir() {
		return sysDir;
	}

	/**
	 * @param sysDir
	 *            设置sysDir属性
	 */
	public void setSysDir(String sysDir) {
		this.sysDir = sysDir;
	}

	/**
	 * @return servicePort属性
	 */
	public int getServicePort() {
		return servicePort;
	}

	/**
	 * @param servicePort
	 *            设置servicePort属性
	 */
	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	/**
	 * @return podName属性
	 */
	public String getPodName() {
		return podName;
	}

	/**
	 * @param podName
	 *            设置podName属性
	 */
	public void setPodName(String podName) {
		this.podName = podName;
	}

}
