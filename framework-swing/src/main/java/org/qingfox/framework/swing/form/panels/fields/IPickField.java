package org.qingfox.framework.swing.form.panels.fields;

import org.qingfox.framework.common.beans.Field;

public interface IPickField<T> extends IField<T> {

	public void setPickModel(IPickFieldModel<T> model);

	public void setFields(Field<T>[] fields);

	public void setField(String code, Field<T> field);

	public void clear();

	public T[] getValues();

	public void addItem(Field<T> field);

	public void removeItem(Field<T> field);

	public boolean hasItem(Field<T> field);

	public Field<T> getSelectItem();

	public Field<T>[] getSelectItems();
}
