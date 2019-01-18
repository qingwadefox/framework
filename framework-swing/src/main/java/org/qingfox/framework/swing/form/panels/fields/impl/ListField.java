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
package org.qingfox.framework.swing.form.panels.fields.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang3.ArrayUtils;

import org.qingfox.framework.common.beans.Field;
import org.qingfox.framework.swing.form.panels.fields.IAutoComplete;
import org.qingfox.framework.swing.form.panels.fields.IPickField;
import org.qingfox.framework.swing.form.panels.fields.IPickFieldModel;

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
public class ListField<T> extends JScrollPane implements IPickField<T>, IAutoComplete<T>, ListSelectionListener {

	private static final long serialVersionUID = 3103946621462566869L;

	private Field<T>[] fields;

	private Field<T>[] autoCompleteFields;

	private JList<String> jList;

	private DefaultComboBoxModel<String> model;

	private List<ListSelectionListener> listSelectionListeners;

	public ListField() {
		super();
		listSelectionListeners = new ArrayList<ListSelectionListener>();
		jList = new JList<String>();
		jList.addListSelectionListener(this);
		this.setViewportView(jList);
	}

	public ListField(Field<T>[] fields) {
		super();
		this.setFields(fields);
	}

	@Override
	public void setFields(Field<T>[] fields) {
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		if (model == null) {
			model = new DefaultComboBoxModel<String>();
		}
		jList.setModel(new DefaultComboBoxModel<String>(fieldNames));
		this.fields = fields;
	}

	@Override
	public T getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field<T> getField(String code) {
		if (fields == null) {
			return null;
		}
		for (Field<T> field : fields) {
			if (field.getCode().equals(code)) {
				return field;
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.swing.form.panels.fields.IField#setValue(java.lang.Object)
	 * @author Administrator 2017年11月23日 Administrator
	 */
	@Override
	public void setValue(T value) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.swing.form.panels.fields.IPickField#setPickModel(org.qingfox.framework.swing.form.panels.fields.IPickFieldModel)
	 * @author Administrator 2017年11月23日 Administrator
	 */
	@Override
	public void setPickModel(IPickFieldModel<T> model) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.swing.form.panels.fields.IPickField#clear()
	 * @author Administrator 2017年11月23日 Administrator
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.swing.form.panels.fields.IPickField#getValues()
	 * @author Administrator 2017年11月23日 Administrator
	 */
	@Override
	public T[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addItem(Field<T> field) {
		if (model == null) {
			model = new DefaultComboBoxModel<String>();
			jList.setModel(model);
		}
		model.addElement(field.getName());
		this.fields = ArrayUtils.add(this.fields, field);

	}

	public void insertItem(Field<T> field, Integer index) {
		if (fields == null) {
			addItem(field);
		} else {
			model.insertElementAt(field.getName(), index);
			fields = ArrayUtils.add(this.fields, index, field);
		}
	}

	@Override
	public void removeItem(Field<T> field) {
		if (this.fields == null || this.fields.length == 0) {
			return;
		}
		model.removeElementAt(ArrayUtils.indexOf(this.fields, field));
		this.fields = ArrayUtils.remove(this.fields, ArrayUtils.indexOf(this.fields, field));
	}

	@Override
	public boolean hasItem(Field<T> field) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Field<T> getSelectItem() {
		if (jList.getSelectedIndex() == -1) {
			return null;
		}
		return this.fields[jList.getSelectedIndex()];
	}

	@Override
	public Field<T>[] getSelectItems() {
		int[] indexs = jList.getSelectedIndices();

		if (indexs == null || indexs.length == 0) {
			return null;
		}
		@SuppressWarnings("unchecked")
		Field<T>[] selectFields = new Field[indexs.length];

		Field<T>[] fields = null;
		if (autoCompleteFields != null) {
			fields = autoCompleteFields;
		} else {
			fields = this.fields;
		}

		for (int i = 0; i < indexs.length; i++) {
			selectFields[i] = fields[indexs[i]];
		}

		return selectFields;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.qingfox.framework.swing.form.panels.fields.IAutoComplete#filter(java.lang.String)
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public void filter(String name) {
		// TODO Auto-generated method stub

	}

	public void addListSelectionListener(ListSelectionListener listener) {
		listSelectionListeners.add(listener);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		e = new ListSelectionEvent(this, e.getFirstIndex(), e.getLastIndex(), e.getValueIsAdjusting());
		for (ListSelectionListener listener : listSelectionListeners) {
			listener.valueChanged(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see org.qingfox.framework.swing.form.panels.fields.IPickField#setField(java.lang.String, com.framework.common.beans.Field)
	 * @author Administrator
	 * 2018年1月12日 Administrator
	 */
	@Override
	public void setField(String code, Field<T> fields) {
		// TODO Auto-generated method stub
		
	}

}
