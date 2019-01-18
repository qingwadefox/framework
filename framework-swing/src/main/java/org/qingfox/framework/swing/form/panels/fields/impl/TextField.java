package org.qingfox.framework.swing.form.panels.fields.impl;

import javax.swing.JTextField;

import org.qingfox.framework.swing.form.panels.fields.IField;

public class TextField extends JTextField implements IField<String> {

	private static final long serialVersionUID = -5364412323276893727L;

	public String getValue() {
		return this.getText();
	}

	public void setValue(String value) {
		this.setText(value);
	}

}
