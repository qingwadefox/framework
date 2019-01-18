package org.qingfox.framework.swing.components.labels;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class SelectLabel extends JLabel {

	private static final long serialVersionUID = 2396744168215953257L;
	private Color textColor;
	private Color borderColor;
	private Color selectColor;
	private boolean selected = false;
	private final static int BORDER_WIDTH = 2;

	public SelectLabel() {
		super();
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		this.setForeground(textColor);
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		Color color = borderColor;
		if (selected) {
			color = selectColor;
		}
		this.setBorder(BorderFactory.createLineBorder(color, BORDER_WIDTH));
	}

	public Color getSelectColor() {
		return selectColor;
	}

	public void setSelectColor(Color selectColor) {
		this.selectColor = selectColor;
		if (selected) {
			this.setBorder(BorderFactory.createLineBorder(selectColor,
					BORDER_WIDTH));
		}
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		Color color = borderColor;
		if (selected) {
			color = selectColor;
		}
		this.setBorder(BorderFactory.createLineBorder(color, BORDER_WIDTH));
	}

	public boolean isSelected() {
		return selected;
	}
}
