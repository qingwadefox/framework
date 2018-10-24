package com.framework.swing.components.table;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.framework.common.utils.ReflectUtil;
import com.framework.swing.beans.ColumnBean;
import com.framework.swing.components.IComponent;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.inf.IColumnRender;

public class TablePanel<T> extends JScrollPane implements IComponent {

	private static final long serialVersionUID = -791623467534300820L;

	private String code;

	private DefaultTableModel tableModel;

	private JTable table;

	private List<ColumnBean> columnBeans;

	private List<T> datas;

	private Map<String, IColumnRender<Object>> columnRenderMap;

	private Map<String, TableCellEditor> cellEditorMap;

	private Map<String, TableCellRender> cellRenderMap;

	public TablePanel() {
		super();
		this.init();
	}

	private void init() {
		datas = new ArrayList<T>();
		columnBeans = new ArrayList<ColumnBean>();
		cellEditorMap = new HashMap<String, TableCellEditor>();
		cellRenderMap = new HashMap<String, TableCellRender>();
		columnRenderMap = new HashMap<String, IColumnRender<Object>>();
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 5253645975693041729L;

			@Override
			public boolean isCellEditable(int row, int column) {
				if (cellEditorMap.get(columnBeans.get(column).getCode()) == null) {
					return false;
				} else {
					return true;
				}
			}
		};
		table = new JTable(tableModel);
		setViewportView(table);
	}

	public JTable getTable() {
		return table;
	}

	public List<T> getDatas() {
		return datas;
	}

	public Integer findRowIndex(String columnCode, Object value) {
		for (int i = 0; i < datas.size(); i++) {
			T data = datas.get(i);
			try {
				Object _value = ReflectUtil.getFieldValue(data, columnCode);
				if (_value.equals(value)) {
					return i;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}

		return -1;
	}

	public void setColumnBeans(List<ColumnBean> columnBeans) {
		for (ColumnBean columnBean : columnBeans) {
			this.addColumnBean(columnBean);
		}
	}

	public void addColumnBean(ColumnBean column) {
		tableModel.addColumn(column.getName());
		columnBeans.add(column);
		refCell();
	}

	public void addColumnBean(String code, String name) {
		this.addColumnBean(new ColumnBean(code, name));
	}

	@SuppressWarnings({"hiding", "unchecked"})
	public <T> void addColumnRender(String columnCode, IColumnRender<T> render) {
		columnRenderMap.put(columnCode, (IColumnRender<Object>) render);
	}

	public void setDatas(List<T> datas) {
		if (datas == null) {
			return;
		}
		for (T data : datas) {
			this.addData(data);
		}
	}

	private TableColumn getColumn(String code) {
		for (ColumnBean column : columnBeans) {
			if (column.getCode().equals(code)) {
				return table.getColumn(column.getName());
			}
		}
		return null;
	}

	public void addData(T data) {
		tableModel.addRow(this.getData(data));
		datas.add(data);
	}

	public void setValue(Integer rowIndex, String columnCode, Object value) {
		T data = datas.get(rowIndex);
		try {
			ReflectUtil.setFieldValue(data, columnCode, value);
			datas.set(rowIndex, data);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		int columnIndex = 0;
		for (ColumnBean column : this.columnBeans) {
			if (column.getCode().equals(columnCode)) {
				break;
			}
			columnIndex++;
		}
		IColumnRender<Object> render = columnRenderMap.get(columnCode);
		tableModel.setValueAt(render == null ? value : render.render(value), rowIndex, columnIndex);
	}

	public void setData(Integer rowIndex, T data) {
		if (rowIndex < 0) {
			return;
		}
		datas.set(rowIndex, data);
		for (int i = 0; i < this.columnBeans.size(); i++) {
			IColumnRender<Object> render = columnRenderMap.get(columnBeans.get(i).getCode());
			Object value = null;
			try {
				value = ReflectUtil.getFieldValue(data, columnBeans.get(i).getCode());
				if (value != null) {
					value = render == null ? value : render.render(value);
				}
				tableModel.setValueAt(value, rowIndex, i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Object[] getData(T data) {
		List<Object> objData = new ArrayList<Object>();
		for (int i = 0; i < columnBeans.size(); i++) {
			String code = columnBeans.get(i).getCode();
			IColumnRender<Object> render = columnRenderMap.get(code);
			Object value = null;
			try {
				value = ReflectUtil.getFieldValue(data, code);
			} catch (Exception e) {
			}
			value = render == null ? value : render.render(value);
			objData.add(value);

		}
		return objData.toArray();
	}

	public int getSelectedRow() {
		return this.table.getSelectedRow();
	}

	public T getSelectData() {
		if (getSelectedRow() != -1) {
			return datas.get(getSelectedRow());
		} else {
			return null;
		}

	}

	public void delSelectedRow() {
		for (int row : this.table.getSelectedRows()) {
			tableModel.removeRow(row);
		}
	}

	private void refCell() {
		for (ColumnBean column : columnBeans) {
			String code = column.getCode();
			TableColumn tableColumn = getColumn(code);
			TableCellEditor editor = cellEditorMap.get(code);
			TableCellRender render = cellRenderMap.get(code);

			if (tableColumn.getCellRenderer() == null && render != null) {
				tableColumn.setCellRenderer(render);
			}

			if (tableColumn.getCellEditor() == null && editor != null) {
				tableColumn.setCellEditor(editor);
			}
		}
	}

	public void clear() {
		datas = new ArrayList<T>();
		while (tableModel.getRowCount() != 0) {
			tableModel.removeRow(tableModel.getRowCount() - 1);
		}
	}

	public void setColumnEditor(String code, IField<?> field) {
		this.setColumnEditor(code, field, 2);
	}

	public void setColumnEditor(String code, IField<?> field, Integer clickCount) {
		TableCellEditor editor = new TableCellEditor(field);
		editor.setClickCountToStart(clickCount);
		cellEditorMap.put(code, editor);
		refCell();
	}

	public void setColumnRender(String code, IField<?> field) {
		TableCellRender tableCellRender = new TableCellRender(field);
		cellRenderMap.put(code, tableCellRender);
		refCell();
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setSize(Integer width, Integer height) {
		this.setSize(new Dimension(width, height));
	}

	public void setWidth(Integer width) {
		setSize(new Dimension(width, this.getHeight()));
	}

	public void setHeight(Integer height) {
		setSize(new Dimension(this.getWidth(), height));
	}

}
