package org.qingfox.framework.swing.form.panels.impl;

import java.awt.Color;

import org.qingfox.framework.swing.form.panels.fields.IPickField;
import org.qingfox.framework.swing.form.panels.fields.impl.ColorPickField;

public class ColorFieldPanel extends PickFieldPanel<Color, ColorPickField> {

	private static final long serialVersionUID = 2861179346273944484L;

	public ColorFieldPanel(String code, String name) {
		super(code, name);
	}

	@Override
	public IPickField<Color> getField() {
		return new ColorPickField();
	}

}
