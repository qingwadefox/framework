package com.framework.swing.form.panels.impl;

import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.LabelField;

public class LabelFieldPanel extends BaseFieldPanel<String, LabelField> {

	private static final long serialVersionUID = -2049284643176839690L;

	public LabelFieldPanel(String code, String name) {
		super(code, name);
	}

	public LabelFieldPanel(String code, String name, String value) {
		super(code, name, value);
	}

	@Override
	public IField<String> getField() {
		return new LabelField();
	}

}
