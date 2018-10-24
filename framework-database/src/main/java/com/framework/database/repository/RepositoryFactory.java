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
package com.framework.database.repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.scanner.PackageScanner;
import com.framework.common.utils.ReflectUtil;
import com.framework.database.dao.IEasyDao;
import com.framework.database.dao.IEntityDao;
import com.framework.database.dao.IMapDao;
import com.framework.database.dao.impl.EasyDaoImpl;
import com.framework.database.dao.impl.EntityDaoImpl;
import com.framework.database.dao.impl.MapDaoImpl;
import com.framework.database.inf.Initializing;
import com.framework.database.stereotypes.Repository;

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
public class RepositoryFactory {

	private static final ILogger logger = LoggerFactory.getLogger(RepositoryFactory.class);

	private static IEasyDao easyDao;
	private static IEntityDao entityDao;
	private static IMapDao mapDao;
	private static Map<String, BaseRepository> repositoryMap;

	public static void init(String scanPackage) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {

		easyDao = new EasyDaoImpl();
		entityDao = new EntityDaoImpl();
		mapDao = new MapDaoImpl();
		repositoryMap = new HashMap<String, BaseRepository>();

		if (StringUtils.isNotEmpty(scanPackage)) {
			logger.info("开始扫描包【", scanPackage, "】");
			PackageScanner scanner = new PackageScanner(scanPackage);
			List<String> list = scanner.scan();
			logger.info("扫描包中类总数为【", list.size(), "】");
			for (String classStr : list) {
				Class<?> _class = Class.forName(classStr, false, RepositoryFactory.class.getClassLoader());
				Repository repositoryAnn = _class.getAnnotation(Repository.class);

				if (repositoryAnn != null) {
					logger.info("开始注册类【", classStr, "】");
					Object repository = (BaseRepository) _class.newInstance();
					((BaseRepository) repository).setEasyDao(easyDao);
					((BaseRepository) repository).setEntityDao(entityDao);
					((BaseRepository) repository).setMapDao(mapDao);
					if (ReflectUtil.existInterfaces(_class, Initializing.class)) {
						((Initializing) repository).afterInitializing();
					}
					repositoryMap.put(classStr, (BaseRepository) repository);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends BaseRepository> T getRepository(Class<T> classz) {
		return (T) repositoryMap.get(classz.getName());

	}
}
