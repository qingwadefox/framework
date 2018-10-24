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
package com.framework.common.log;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.framework.common.utils.DateUtil;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;
import ch.qos.logback.core.util.OptionHelper;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2015年12月5日
 * @功能说明：
 * 
 */
public class MDCRegBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
	private String key;
	private String defaultValue;
	private String reg;

	public String getDiscriminatingValue(ILoggingEvent event) {
		Pattern p = Pattern.compile("\\{.*?\\}");// 查找规则公式中大括号以内的字符
		Matcher m = p.matcher(this.reg);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {// 遍历找到的所有大括号
			String param = m.group();// 去掉括号
			String mdcKey = param.substring(1, param.length() - 1);
			String mdcValue;

			if (mdcKey.equals("timestamp")) {
				mdcValue = DateUtil.format(new Date(event.getTimeStamp()), DateUtil.PATTERN_YYYYMMDD);
			} else {
				mdcValue = event.getMDCPropertyMap().get(mdcKey);
			}
			mdcValue = mdcValue == null ? "" : mdcValue;
			m.appendReplacement(sb, mdcValue);
		}
		m.appendTail(sb);

		return sb.toString();
	}

	public void start() {
		int errors = 0;
		if (OptionHelper.isEmpty(this.key)) {
			++errors;
			addError("The \"Key\" property must be set");
		}
		if (OptionHelper.isEmpty(this.defaultValue)) {
			++errors;
			addError("The \"DefaultValue\" property must be set");
		}
		if (errors == 0)
			this.started = true;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return reg属性
	 */
	public String getReg() {
		return reg;
	}

	/**
	 * @param reg
	 *            设置reg属性
	 */
	public void setReg(String reg) {
		this.reg = reg;
	}

}
