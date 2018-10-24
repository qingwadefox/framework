package com.framework.swing.components.table;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.qingfox.framework.common.utils.ReflectUtil;
import com.framework.swing.form.panels.fields.IField;

public class TableCellRender extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 4695702266163018995L;

	private IField<?> field;

	public TableCellRender() {
		super();
	}

	public TableCellRender(IField<?> field) {
		super();
		this.field = field;
	}

	@Override
	protected void setValue(Object value) {
		if (field != null) {
			try {
				ReflectUtil.invokMethodUnmatch(field, "setValue", value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.setValue(value);

	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component _component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (field != null) {
			_component = (Component) field;
		}
		return _component;
	}

}
