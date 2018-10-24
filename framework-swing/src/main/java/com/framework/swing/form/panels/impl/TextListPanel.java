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
package com.framework.swing.form.panels.impl;

import org.apache.commons.lang3.ArrayUtils;

import com.framework.common.beans.Field;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.ListField;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年11月23日
 * @功能说明：
 * 
 */
public class TextListPanel extends BaseFieldPanel<String, ListField<String>> {

	private static final long serialVersionUID = -6500767336477169015L;

	public TextListPanel(String code) {
		super(code);
	}

	public TextListPanel(String code, String name) {
		super(code, name);
	}
	public void setTextFields(String[] texts) {
		Field<String>[] fields = null;
		for (String text : texts) {
			Field<String> field = new Field<String>();
			field.setName(text);
			field.setValue(text);
			fields = ArrayUtils.add(fields, field);
			super.getComponent().setFields(fields);
		}
	}
	@Override
	public IField<String> getField() {
		return new ListField<String>();
	}
	public void setFields(Field<String>[] fields) {
		super.getComponent().setFields(fields);
	}

}
