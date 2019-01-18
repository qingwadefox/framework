package org.qingfox.framework.swing.enums;

import java.awt.Color;

public enum ColorEnum {

	RED(Color.RED, "红", "RED"),

	WHITE(Color.WHITE, "白", "WHITE"),

	ORANGE(Color.ORANGE, "橙", "ORANGE"),

	YELLOW(Color.YELLOW, "黄", "YELLOW"),

	BLUE(Color.BLUE, "蓝", "BLUE"),

	GREEN(Color.GREEN, "绿", "GREEN"),

	TRANSPARENT(new Color(0, 0, 0, 0), "透明", "TRANSPARENT");

	private Color color;
	private String text;
	private String code;

	ColorEnum(Color color, String text, String code) {
		this.text = text;
		this.color = color;
		this.code = code;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static ColorEnum getEnmu(Color color) {

		if (color != null) {
			for (ColorEnum _enum : ColorEnum.values()) {
				if (_enum.getColor().equals(color)) {
					return _enum;
				}
			}
		}
		return null;
	}

	public static ColorEnum getEnmu(String code) {

		if (code != null) {
			for (ColorEnum _enum : ColorEnum.values()) {
				if (_enum.getCode().equals(code)) {
					return _enum;
				}
			}
		}
		return null;
	}
}
