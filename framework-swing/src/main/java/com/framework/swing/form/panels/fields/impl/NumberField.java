package com.framework.swing.form.panels.fields.impl;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;

import com.framework.swing.form.panels.fields.IField;

public class NumberField extends JTextField implements IField<String>,
		KeyListener, FocusListener {

	private static final long serialVersionUID = 505028716257703189L;

	private Double maxValue;
	private Double minValue;
	private Integer pointCount;

	public NumberField() {
		super();
		init();
	}

	private void init() {
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setText("0");
	}

	public void keyTyped(KeyEvent e) {
		String text = this.getText();
		int point = this.getCaretPosition();
		char keyCh = e.getKeyChar();
		switch (keyCh) {
		case '.':
			if (text.indexOf('.') != -1) {
				e.setKeyChar('\0');
				break;
			}

			if (point == 0) {
				e.setKeyChar('\0');
				break;
			}

			if (pointCount == null || pointCount.equals(0)) {
				e.setKeyChar('\0');
				break;
			}

			if (text.length() - point > pointCount) {
				e.setKeyChar('\0');
				break;
			}

		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			if (text.indexOf('.') != -1 && point > text.indexOf('.')) {
				if (text.length() - text.indexOf(".") > pointCount) {
					e.setKeyChar('\0');
					break;
				}
			}
			break;
		default:
			e.setKeyChar('\0');
			break;

		}

		// if ((keyCh < '0') || (keyCh > '9')) {
		//
		// if (keyCh)
		//
		// }
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Integer getPointCount() {
		return pointCount;
	}

	public void setPointCount(Integer pointCount) {
		this.pointCount = pointCount;
	}

	public void focusLost(FocusEvent e) {
		String text = this.getText();
		this.setValue(text);
	}

	public void focusGained(FocusEvent e) {
		this.select(0, this.getText().length());
		return;

	}

	public void keyPressed(KeyEvent e) {
		return;
	}

	public void keyReleased(KeyEvent e) {
		return;
	}

	public String getValue() {
		return this.getText();
	}

	public void setValue(String value) {
		if (StringUtils.isEmpty(value)) {
			value = "0";
		}
		BigDecimal bigDecimal = new BigDecimal(value);
		if (pointCount != null) {
			bigDecimal.setScale(pointCount);
		}
		this.setText(bigDecimal.toString());
	}

	public void setValue(Integer value) {
		this.setValue(value.toString());
	}

	public BigDecimal getBidec() {
		BigDecimal bigDecimal = new BigDecimal(this.getText());
		if (pointCount != null) {
			bigDecimal.setScale(pointCount);
		}
		return bigDecimal;
	}
}
