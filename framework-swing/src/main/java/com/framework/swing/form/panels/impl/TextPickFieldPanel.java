package com.framework.swing.form.panels.impl;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.form.panels.fields.IPickField;
import com.framework.swing.form.panels.fields.impl.TextPickField;

public class TextPickFieldPanel extends PickFieldPanel<String, TextPickField> {

	private static final long serialVersionUID = 1855289474599603024L;

	public TextPickFieldPanel(String code, String name) {
		super(code, name);
	}

	public TextPickFieldPanel(String code, String name, Field<String>[] fields) {
		super(code, name, fields);
	}

	@Override
	public IPickField<String> getField() {
		return new TextPickField();
	}
}
