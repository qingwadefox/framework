package com.framework.swing.form.panels.fields.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.components.panels.LayoutPanel;
import com.framework.swing.form.panels.fields.IPickField;
import com.framework.swing.form.panels.fields.IPickFieldModel;

public class CheckField<T> extends LayoutPanel implements IPickField<T> {

	private static final long serialVersionUID = -2097106594137308549L;

	private Field<T>[] fields;

	private List<JCheckBox> ckList;

	public CheckField() {
		super();
	}

	public CheckField(Field<T>[] fields) {
		super();
		this.setFields(fields);
	}

	public T getValue() {
		for (int i = 0; i < ckList.size(); i++) {
			if (ckList.get(i).isSelected()) {
				return fields[i].getValue();
			}
		}
		return null;
	}

	public void setValue(T value) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getValue().equals(value)) {
				ckList.get(i).setSelected(true);
			} else {
				ckList.get(i).setSelected(false);
			}
		}
	}

	public void setPickModel(IPickFieldModel<T> model) {
		// TODO Auto-generated method stub

	}

	public void setFields(Field<T>[] fields) {
		this.fields = fields;
		this.setInsets(2, 2, 2, 2);
		ckList = new ArrayList<JCheckBox>();
		for (Field<T> field : fields) {
			JCheckBox checkBox = new JCheckBox(field.getName());
			checkBox.setBackground(null);
			addComponent(checkBox);
			ckList.add(checkBox);
		}

	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public T[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.framework.swing.form.panels.fields.IPickField#addItem(com.framework.common.beans.Field)
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public void addItem(Field<T> field) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.framework.swing.form.panels.fields.IPickField#removeItem(com.framework.common.beans.Field)
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public void removeItem(Field<T> field) {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.framework.swing.form.panels.fields.IPickField#hasItem(com.framework.common.beans.Field)
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public boolean hasItem(Field<T> field) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.framework.swing.form.panels.fields.IPickField#getSelectItem()
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public Field<T> getSelectItem() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.framework.swing.form.panels.fields.IPickField#getSelectItems()
	 * @author Administrator
	 * 2017年11月24日 Administrator
	 */
	@Override
	public Field<T>[] getSelectItems() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see com.framework.swing.form.panels.fields.IPickField#setField(java.lang.String, com.framework.common.beans.Field)
	 * @author Administrator
	 * 2018年1月12日 Administrator
	 */
	@Override
	public void setField(String code, Field<T> fields) {
		// TODO Auto-generated method stub
		
	}

}
