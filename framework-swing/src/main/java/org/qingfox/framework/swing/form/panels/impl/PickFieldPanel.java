package org.qingfox.framework.swing.form.panels.impl;

import org.qingfox.framework.common.beans.Field;
import org.qingfox.framework.swing.form.panels.fields.IPickField;
import org.qingfox.framework.swing.form.panels.fields.impl.PickField;

public class PickFieldPanel<T, F extends IPickField<T>> extends
		BaseFieldPanel<T, PickField<T>> {

	private static final long serialVersionUID = 5392434555535065643L;

	public PickFieldPanel(String code, String name) {
		super(code, name);
	}

	public PickFieldPanel(String code, String name, Field<T>[] fields) {
		super(code, name);
		this.setFields(fields);
	}

	@Override
	public IPickField<T> getField() {
		return new PickField<T>();
	}

	public void setFields(Field<T>[] fields) {
		super.getComponent().setFields(fields);
	}

	public void clear() {
		((IPickField<T>) super.getField()).clear();
	}
}
