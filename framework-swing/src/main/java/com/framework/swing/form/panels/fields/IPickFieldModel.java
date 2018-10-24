package com.framework.swing.form.panels.fields;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.components.labels.SelectLabel;

public interface IPickFieldModel<T> {
	public void setSelectLabel(Field<T> field, SelectLabel label);
}
