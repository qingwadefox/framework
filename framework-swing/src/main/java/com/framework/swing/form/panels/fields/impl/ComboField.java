package com.framework.swing.form.panels.fields.impl;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.form.panels.fields.IPickField;
import com.framework.swing.form.panels.fields.IPickFieldModel;

public class ComboField<T> extends JComboBox<String> implements IPickField<T>, KeyListener {

	private static final long serialVersionUID = 5561952487423015952L;

	private Field<T>[] fields;

	private Field<T>[] autoCompleteFields;

	private DefaultComboBoxModel<String> model;

	public ComboField() {
		super();
	}

	public ComboField(Field<T>[] fields) {
		super();
		this.setFields(fields);
	}

	public void setFields(Field<T>[] fields) {
		this.initModel(fields);
		this.fields = fields;
	}

	private void initModel(Field<T>[] fields) {
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		model = new DefaultComboBoxModel<String>(fieldNames);
		setModel(model);
	}

	public void addItem(Field<T> field) {
		fields = ArrayUtils.add(fields, field);
		this.initModel(fields);
	}

	public void removeItem(Field<T> field) {
		fields = ArrayUtils.removeElement(fields, field);
		this.initModel(fields);
	}

	public void setAutoComplete(Boolean autoComplete) {
		if (autoComplete) {
			this.setEditable(true);
			this.getEditor().getEditorComponent().addKeyListener(this);
		}
	}

	private void doAutoComplete(String str, int caretPosition) {
		if (fields == null || fields.length == 0) {
			return;
		}

		@SuppressWarnings("unchecked")
		Field<T>[] _fields = new Field[0];

		if (StringUtils.isEmpty(str)) {
			autoCompleteFields = null;
			_fields = this.fields;
		} else {
			for (Field<T> _field : fields) {
				String fieldName = _field.getName();
				if (fieldName.indexOf(str) != -1) {
					_fields = ArrayUtils.add(_fields, _field);
				}
			}
			autoCompleteFields = _fields;
		}

		String[] fieldNames = new String[_fields.length];
		for (int i = 0; i < _fields.length; i++) {
			fieldNames[i] = _fields[i].getName();
		}
		setModel(new DefaultComboBoxModel<String>(fieldNames));

		if (_fields.length > 0) {
			if (caretPosition > this.getEditorComponent().getText().length()) {
				return;
			}
			this.getEditorComponent().setCaretPosition(caretPosition);
			this.getEditorComponent().setText(this.getEditorComponent().getText().trim().substring(0, caretPosition));
			this.showPopup();
		}

	}
	@SuppressWarnings("unchecked")
	public Field<T>[] getFields() {
		if (fields == null) {
			return new Field[0];
		} else {
			return fields;
		}
	}

	public T getValue() {
		if (autoCompleteFields != null) {
			return autoCompleteFields[this.getSelectedIndex()].getValue();
		} else if (fields != null) {
			return fields[this.getSelectedIndex()].getValue();
		}
		return null;
	}

	public void setValue(T value) {
		int i = 0;
		for (Field<T> field : fields) {
			if (field.getValue().equals(value)) {
				break;
			}
			i++;
		}
		this.setSelectedIndex(i);
	}

	public void clear() {

	}

	public void setPickModel(IPickFieldModel<T> model) {
		// TODO Auto-generated method stub

	}

	public T[] getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 * @author Administrator 2017年11月22日 Administrator
	 */
	@Override
	public void keyTyped(KeyEvent e) {

	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 * @author Administrator 2017年11月22日 Administrator
	 */
	@Override
	public void keyPressed(KeyEvent e) {

	}

	private JTextComponent getEditorComponent() {
		return (JTextComponent) this.getEditor().getEditorComponent();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 * @author Administrator 2017年11月22日 Administrator
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch == KeyEvent.VK_ENTER) {
			if (this.getSelectedIndex() == -1 && this.autoCompleteFields != null) {
				this.setSelectedIndex(0);
			}
			return;

		}
		if (ch == KeyEvent.CHAR_UNDEFINED || ch == KeyEvent.VK_DELETE) {
			return;
		}
		JTextField jtf = ((JTextField) this.getEditor().getEditorComponent());
		String str = jtf.getText();
		int caretPosition = this.getEditorComponent().getCaretPosition();
		doAutoComplete(str, caretPosition);

	}

	@Override
	public boolean hasItem(Field<T> field) {
		if (fields == null) {
			return false;
		}
		for (Field<T> _field : fields) {
			if (_field.equalsField(field)) {
				return true;
			}
		}
		return false;
	}

	public Field<T> getSelectItem() {
		if (autoCompleteFields != null) {
			return autoCompleteFields[this.getSelectedIndex()];
		} else {
			return fields[this.getSelectedIndex()];
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see com.framework.swing.form.panels.fields.IPickField#getSelectItems()
	 * @author Administrator 2017年11月24日 Administrator
	 */
	@Override
	public Field<T>[] getSelectItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setField(String code, Field<T> field) {
		int selectIndex = getSelectedIndex();
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getCode().equals(code)) {
				fields[i] = field;
				model.removeElementAt(i);
				model.insertElementAt(fields[i].getName(), i);
				if (selectIndex == i) {
					setSelectedIndex(selectIndex);
				}
				return;
			}
		}
	}

}
