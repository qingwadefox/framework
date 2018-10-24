package com.framework.swing.components.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.utils.RandomUtil;
import com.framework.swing.components.IComponent;
import com.framework.swing.components.ILayoutComponent;
import com.framework.swing.exceptions.ExistComponentsException;
import com.framework.swing.factory.ComponentFactory;

public class LayoutDialog extends JDialog implements ILayoutComponent,
		IComponent {
	private static final long serialVersionUID = 5026151296584957571L;
	private final static Integer DEFAULT_WIDTH = 800;
	private final static Integer DEFAULT_HEIGHT = 600;
	private GridBagConstraints gridBagConstraints;
	private Map<String, Component> componentMap;
	private Integer maxX = 0;
	private Integer maxY = 0;
	private Insets insets;
	private String code;
	private int fill = GridBagConstraints.BOTH;
	private Frame frame;

	public LayoutDialog(Frame frame) {
		super(frame);
		this.frame = frame;
		this.defaultInit(null, null, null, null);
	}

	public LayoutDialog(Frame frame, String code) {
		super(frame, "");
		this.frame = frame;
		this.defaultInit(null, null, null, null);
	}

	public LayoutDialog(Frame frame, String code, String title) {
		super(frame, title);
		this.frame = frame;
		this.defaultInit(code, null, null, null);
	}

//	public LayoutDialog(Frame frame, String code, String title, Integer width,
//			Integer height) {
//		super(frame, title);
//		this.frame = frame;
//		this.defaultInit(code, title, width, height);
//	}

	private void defaultInit(String code, String title, Integer width,
			Integer height) {

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
		if (width == null) {
			width = DEFAULT_WIDTH;
		}
		if (height == null) {
			height = DEFAULT_HEIGHT;
		}
		super.setSize(width, height);
		ComponentFactory.init(this);
		setLocationRelativeTo(frame);
		initLayout();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		componentMap = new HashMap<String, Component>();
		insets = new Insets(0, 0, 0, 0);
	}

	public void setTitle(String title) {
		this.setTitle(title);
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

	public void addComponent(Component component, int x, int y, int width,
			int height) {

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
			throw new ExistComponentsException();
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
		for (int i = x; i < width; i++) {
			for (int j = y; j < height; j++) {
				componentMap.put(i + "_" + j, component);
			}
		}
	}

	public void setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
	}

	public void setFill(int fill) {
		this.fill = fill;
	}

}
