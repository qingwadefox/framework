package com.framework.swing.form.panels.impl;

import java.math.BigDecimal;

import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.NumberField;

public class NumberFieldPanel extends BaseFieldPanel<String, NumberField> {

	private static final long serialVersionUID = 6753681524524141005L;

	public NumberFieldPanel(String code, String name) {
		super(code, name);
	}

	public NumberFieldPanel(String code, String name, String value) {
		super(code, name, value);
	}

	@Override
	public IField<String> getField() {
		return new NumberField();
	}

	public BigDecimal getBidec() {
		return super.getComponent().getBidec();
	}

}
