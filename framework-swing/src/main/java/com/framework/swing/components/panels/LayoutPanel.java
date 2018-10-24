package com.framework.swing.components.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.utils.RandomUtil;
import com.framework.swing.components.IComponent;
import com.framework.swing.components.ILayoutComponent;
import com.framework.swing.exceptions.ExistComponentsException;
import com.framework.swing.factory.ComponentFactory;

public class LayoutPanel extends JPanel implements ILayoutComponent, IComponent {

	private static final long serialVersionUID = -1989932317375744151L;
	private GridBagConstraints gridBagConstraints;
	private Map<String, Component> componentMap;
	private Integer maxX = 0;
	private Integer maxY = 0;
	private Insets insets;
	private String code;
	private int fill = GridBagConstraints.BOTH;

	public LayoutPanel() {
		this.defaultInit(null, null, null, null);
	}

	public LayoutPanel(String code) {
		this.defaultInit(code, null, null, null);
	}

	public LayoutPanel(String code, String title) {
		this.defaultInit(code, title, null, null);
	}

	public LayoutPanel(String code, String title, Integer width, Integer height) {
		this.defaultInit(code, title, width, height);
	}

	private void defaultInit(String code, String title, Integer width, Integer height) {
		this.setBackground(null);

		// framecode
		if (StringUtils.isEmpty(code)) {
			code = RandomUtil.getUUID();
		}
		this.setCode(code);

		// title
		if (StringUtils.isNotEmpty(title)) {
			this.setTitle(title);
		}

		// SIZE
		if (width != null && height != null) {
			this.setSize(width, height);
		}
		ComponentFactory.init(this);
		initLayout();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		componentMap = new HashMap<String, Component>();
		insets = new Insets(0, 0, 0, 0);
	}

	public void setTitle(String title) {
		this.setBorder(new TitledBorder(title));
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;

	}

	public void setSize(Integer width, Integer height) {
		this.setSize(new Dimension(width, height));

	}

	public void setWidth(Integer width) {
		setSize(new Dimension(width, this.getHeight()));

	}

	public void setHeight(Integer height) {
		setSize(new Dimension(this.getWidth(), height));

	}

	public Component getComponent(int x, int y) {
		String key = x + "_" + y;
		return componentMap.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getComponent(Class<T> cmpClass, int x, int y) {
		String key = x + "_" + y;
		return (T) componentMap.get(key);
	}

	public void addComponent(Component component) {
		int x = maxX;
		if (!(maxX == 0 && maxY == 0 && componentMap.get("0_0") == null)) {
			x++;
		}
		this.addComponent(component, x, maxY, 1, 1);
	}

	public void addComponent(Component component, int x, int y) {
		this.addComponent(component, x, y, 1, 1);
	}

	public void addComponent(Component component, int x, int y, int width, int height) {

		int endX = x + (width - 1);
		int endY = y + (height - 1);

		if (endX > maxX) {
			maxX = endX;
		}
		if (endY > maxY) {
			maxY = endY;
		}

		Component _component = componentMap.get(x + "_" + y);
		if (_component != null) {
			throw new ExistComponentsException("x : " + x + " ;  y : " + y + "  Exist Components !");
		}
		gridBagConstraints.fill = fill;
		gridBagConstraints.weightx = width * 10 + 1;
		gridBagConstraints.weighty = height * 10 + 2;
		gridBagConstraints.gridx = x;
		gridBagConstraints.gridy = y;
		gridBagConstraints.gridwidth = width;
		gridBagConstraints.gridheight = height;
		gridBagConstraints.insets = insets;
		this.add(component, gridBagConstraints);
		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				componentMap.put(i + "_" + j, component);
			}
		}
		this.setVisible(true);
	}
	public void setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
	}

	public void setFill(int fill) {
		this.fill = fill;
	}

}
