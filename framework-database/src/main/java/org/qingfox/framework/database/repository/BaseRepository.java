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
package org.qingfox.framework.database.repository;

import org.qingfox.framework.database.dao.IEasyDao;
import org.qingfox.framework.database.dao.IEntityDao;
import org.qingfox.framework.database.dao.IMapDao;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年7月20日
 * @功能说明：
 * 
 */
public class BaseRepository {
	protected IEasyDao easyDao;
	protected IEntityDao entityDao;
	protected IMapDao mapDao;

	/**
	 * @param easyDao
	 *            设置easyDao属性
	 */
	public void setEasyDao(IEasyDao easyDao) {
		this.easyDao = easyDao;
	}

	/**
	 * @param entityDao
	 *            设置entityDao属性
	 */
	public void setEntityDao(IEntityDao entityDao) {
		this.entityDao = entityDao;
	}

	/**
	 * @param mapDao
	 *            设置mapDao属性
	 */
	public void setMapDao(IMapDao mapDao) {
		this.mapDao = mapDao;
	}

}
