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
package org.qingfox.framework.microservice.bean;

import java.io.Serializable;

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
public class ServiceBean implements Serializable {
	private static final long serialVersionUID = -1136556133610719690L;
	private String packagePath;
	private String[] paths;
	private String name;

	public ServiceBean(String packagePath, String name, String[] paths) {
		super();
		this.packagePath = packagePath;
		this.paths = paths;
		this.name = name;
	}
	/**
	 * @return name属性
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 *            设置name属性
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return packagePath属性
	 */
	public String getPackagePath() {
		return packagePath;
	}
	/**
	 * @param packagePath
	 *            设置packagePath属性
	 */
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	/**
	 * @return paths属性
	 */
	public String[] getPaths() {
		return paths;
	}
	/**
	 * @param paths
	 *            设置paths属性
	 */
	public void setPaths(String[] paths) {
		this.paths = paths;
	}
}
