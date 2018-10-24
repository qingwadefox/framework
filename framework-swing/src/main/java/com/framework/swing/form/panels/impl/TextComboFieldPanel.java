package com.framework.swing.form.panels.impl;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.TextComboField;

public class TextComboFieldPanel extends ComboFieldPanel<String, TextComboField> {

	private static final long serialVersionUID = 752280863323162660L;

	public TextComboFieldPanel(String code, String name) {
		super(code, name);
	}

	public TextComboFieldPanel(String code, String name, Field<String>[] fields) {
		super(code, name, fields);
	}

	@Override
	public IField<String> getField() {
		return new TextComboField();
	}

}
