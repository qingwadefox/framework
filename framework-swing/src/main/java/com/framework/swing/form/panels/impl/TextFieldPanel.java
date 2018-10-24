package com.framework.swing.form.panels.impl;

import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.TextField;

public class TextFieldPanel extends BaseFieldPanel<String, TextField> {

	private static final long serialVersionUID = 6683961148043750355L;

	public TextFieldPanel(String code, String name) {
		super(code, name);
	}

	public TextFieldPanel(String code, String name, String value) {
		super(code, name, value);
	}

	@Override
	public IField<String> getField() {
		return new TextField();
	}

}
