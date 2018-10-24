package com.framework.swing.form.panels.fields.impl;

import javax.swing.JLabel;

import com.framework.swing.form.panels.fields.IField;

public class LabelField extends JLabel implements IField<String> {

	private static final long serialVersionUID = 4001882081210637648L;

	public String getValue() {
		return this.getText();
	}

	public void setValue(String value) {
		this.setText(value);
	}

}
