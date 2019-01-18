package com.framewrok.swing.test;

import org.qingfox.framework.swing.components.frames.LayoutFrame;
import org.qingfox.framework.swing.form.panels.fields.impl.DateField;

public class AppTest {
	public static void main(String[] args) {

		LayoutFrame frame = new LayoutFrame();
		DateField dateField = new DateField();
		frame.addComponent(dateField);

		frame.setVisible(true);
	}
}
