package com.framework.swing.form.panels.impl;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.ComboField;

public class ComboFieldPanel<T, F extends ComboField<T>> extends BaseFieldPanel<T, ComboField<T>> {

	private static final long serialVersionUID = 5447689237079723127L;

	public ComboFieldPanel(String code, String name) {
		super(code, name);
	}

	public ComboFieldPanel(String code, String name, Field<T>[] fields) {
		super(code, name);
		this.getComponent().setFields(fields);
	}

	@Override
	public IField<T> getField() {
		return new ComboField<T>();
	}

}
