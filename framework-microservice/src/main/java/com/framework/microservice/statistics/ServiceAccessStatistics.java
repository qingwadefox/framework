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
package com.framework.microservice.statistics;

import java.util.HashMap;
import java.util.Map;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年5月22日
 * @功能说明：
 * 
 */
public class ServiceAccessStatistics {
	private static Map<String, Map<String, Integer>> statisticsValue;

	static {
		statisticsValue = new HashMap<String, Map<String, Integer>>();
	}

	public static void add(String serviceName, String ip) {
		Map<String, Integer> map = statisticsValue.get(serviceName);
		if (map == null) {
			map = new HashMap<String, Integer>();
		}
		Integer count = map.get(ip);
		if (count == null) {
			count = 1;
		} else {
			count += 1;
		}
		map.put(ip, count);
		statisticsValue.put(serviceName, map);
	}

	public static Map<String, Map<String, Integer>> getStatisticsValue() {
		return statisticsValue;
	}

}
