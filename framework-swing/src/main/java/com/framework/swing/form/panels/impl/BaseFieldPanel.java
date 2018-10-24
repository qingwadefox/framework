package com.framework.swing.form.panels.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.framework.swing.factory.ComponentFactory;
import com.framework.swing.form.panels.IFieldPanel;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.utils.SwingUtil;

public class BaseFieldPanel<T, F extends IField<T>> extends JPanel implements IFieldPanel<T, F> {

	private static final long serialVersionUID = 7786328816772669321L;

	private static String SPACE_CHARACTER = "：";

	private String name;

	private JLabel nameLabel;

	private IField<T> field;

	private String code;

	public BaseFieldPanel(String code) {
		super();
		defaultInit(code, null, null);
	}

	public BaseFieldPanel(String code, String name) {
		super();
		defaultInit(code, name, null);
	}

	public BaseFieldPanel(String code, String name, T value) {
		super();
		defaultInit(code, name, value);
	}

	public void defaultInit(String code, String name, T value) {
		this.setBackground(null);
		this.setCode(code);
		BorderLayout borderLayout = new BorderLayout(5, 1);
		this.setLayout(borderLayout);
		nameLabel = new JLabel();
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		nameLabel.setPreferredSize(new Dimension(ComponentFactory.getLabelSize(), this.getHeight()));
		if (name == null) {
			nameLabel.setVisible(false);
		}
		this.add(nameLabel, BorderLayout.WEST);
		this.add(SwingUtil.createSpaceLabel(), BorderLayout.SOUTH);
		this.setCode(code);
		this.setName(name);
		field = this.getField();
		if (field != null) {
			((Component) field).setMinimumSize(new Dimension(0, 0));
			((Component) field).setPreferredSize(new Dimension(this.getWidth(), ComponentFactory.getFieldPanelHeight()));
			// this.setPreferredSize(new Dimension(this.getWidth(),
			// ComponentFactory.getFieldPanelHeight()));
			this.add((Component) field, BorderLayout.CENTER);
		}
		if (field != null && value != null) {
			this.setValue(value);
		}
	}

	public JLabel getNameLabel() {
		return nameLabel;
	}

	public void setError(String error) {
		this.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.PINK));
	}

	public IField<T> getField() {
		return field;
	}

	public T getValue() {
		return field.getValue();
	}

	public void setValue(T value) {
		field.setValue(value);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		nameLabel.setText(name + SPACE_CHARACTER);
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public F getComponent() {
		return (F) field;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

}
