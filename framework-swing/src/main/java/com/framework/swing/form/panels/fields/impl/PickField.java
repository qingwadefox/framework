package com.framework.swing.form.panels.fields.impl;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import com.framework.common.beans.Field;
import com.framework.swing.components.labels.SelectLabel;
import com.framework.swing.components.panels.LayoutPanel;
import com.framework.swing.form.panels.fields.IPickField;
import com.framework.swing.form.panels.fields.IPickFieldModel;

public class PickField<T> extends LayoutPanel implements IPickField<T>, MouseListener {

	private static final long serialVersionUID = 7245067093248924842L;

	private Field<T>[] fields;
	private List<SelectLabel> valueLabels;
	private IPickFieldModel<T> model;

	private Color textColor = Color.DARK_GRAY;
	private Color borderColor = Color.lightGray;
	private Color selectColor = Color.BLACK;

	public PickField() {
		super();
	}

	public PickField(Field<T>[] fields) {
		super();
		this.setFields(fields);
	}

	public T getValue() {
		for (int i = 0; i < valueLabels.size(); i++) {
			if (valueLabels.get(i).isSelected()) {
				return fields[i].getValue();
			}
		}
		return null;
	}

	public void setValue(T value) {
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getValue().equals(value)) {
				valueLabels.get(i).setSelected(true);
			} else {
				valueLabels.get(i).setSelected(false);
			}
		}
	}

	public void setPickModel(IPickFieldModel<T> model) {
		this.model = model;
	}

	public void setFields(Field<T>[] fields) {
		this.fields = fields;
		if (model == null) {
			model = new IPickFieldModel<T>() {
				public void setSelectLabel(Field<T> field, SelectLabel label) {
					label.setHorizontalAlignment(JLabel.CENTER);
					label.setText(field.getName());
				}
			};
		}
		this.setInsets(2, 2, 2, 2);
		valueLabels = new ArrayList<SelectLabel>();
		for (Field<T> field : fields) {
			SelectLabel label = new SelectLabel();
			label.setBackground(null);
			model.setSelectLabel(field, label);
			if (label.getTextColor() == null) {
				label.setTextColor(this.textColor);
			}
			if (label.getSelectColor() == null) {
				label.setSelectColor(this.selectColor);
			}
			if (label.getBorderColor() == null) {
				label.setBorderColor(this.borderColor);
			}
			label.addMouseListener(this);
			this.addComponent(label);
			valueLabels.add(label);
		}
	}

	public T[] getValues() {
		return null;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public void setSelectColor(Color selectColor) {
		this.selectColor = selectColor;
	}

	public void mouseClicked(MouseEvent e) {
		for (SelectLabel label : valueLabels) {
			if (label.equals(e.getSource())) {
				label.setSelected(true);
			} else {
				label.setSelected(false);
			}
		}
	}

	public void clear() {
		for (SelectLabel label : valueLabels) {
			label.setSelected(false);
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

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
