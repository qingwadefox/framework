package com.framework.swing.form.panels.fields.impl;

import java.awt.Color;

import org.qingfox.framework.common.beans.Field;
import com.framework.swing.components.labels.SelectLabel;
import com.framework.swing.enums.ColorEnum;
import com.framework.swing.form.panels.fields.IPickFieldModel;

public class ColorPickField extends PickField<Color> {

	private static final long serialVersionUID = -504823567423279315L;

	public ColorPickField() {
		super();
		IPickFieldModel<Color> model = new IPickFieldModel<Color>() {
			public void setSelectLabel(Field<Color> field, SelectLabel label) {
				Color value = field.getValue();
				label.setBackground(value);
				label.setOpaque(true);
				label.setText("  ");

			}
		};
		this.setPickModel(model);
		Color[] colors = new Color[] { Color.WHITE, Color.RED, Color.YELLOW,
				Color.BLUE, Color.GREEN };
		@SuppressWarnings("unchecked")
		Field<Color>[] fields = (Field<Color>[]) new Field<?>[colors.length];
		for (int i = 0; i < colors.length; i++) {
			Field<Color> field = new Field<Color>();
			ColorEnum ce = ColorEnum.getEnmu(colors[i]);
			field.setName(ce.getText());
			field.setCode(ce.getCode());
			field.setValue(colors[i]);
			fields[i] = field;
		}
		this.setFields(fields);
	}
}
