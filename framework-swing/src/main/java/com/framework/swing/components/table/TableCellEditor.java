package com.framework.swing.components.table;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.qingfox.framework.common.utils.ReflectUtil;
import com.framework.swing.form.panels.fields.IField;

public class TableCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -36732469905257597L;

	private IField<?> field;

	public TableCellEditor(IField<?> field) {
		super(new JTextField());
		this.field = field;
	}

	public TableCellEditor() {
		super(new JTextField());
	}

	@Override
	public Object getCellEditorValue() {
		if (field != null) {
			return field.getValue();
		} else {
			return super.getCellEditorValue();
		}
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		Component _component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
		if (field != null) {
			try {
				ReflectUtil.invokMethodUnmatch(field, "setValue", value);
			} catch (Exception e) {
				e.printStackTrace();
			}
			_component = (Component) field;
		}

		return _component;

	}
}
