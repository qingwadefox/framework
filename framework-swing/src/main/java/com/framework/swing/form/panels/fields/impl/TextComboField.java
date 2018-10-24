package com.framework.swing.form.panels.fields.impl;

import com.framework.common.beans.Field;

public class TextComboField extends ComboField<String> {

	private static final long serialVersionUID = 7743928911145202105L;

	public TextComboField() {
		super();
	}

	public TextComboField(Field<String>[] fields) {
		super();
		super.setFields(fields);
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
	}

}
