package com.framework.swing.components.frames;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import com.framework.swing.components.ILayoutComponent;
import com.framework.swing.exceptions.ExistComponentsException;

public class LayoutFrame extends BaseFrame implements ILayoutComponent {

	private static final long serialVersionUID = -2680847374131811784L;
	private GridBagConstraints gridBagConstraints;
	private Map<String, Component> componentMap;
	private Integer maxX = 0;
	private Integer maxY = 0;
	private Insets insets;

	public LayoutFrame() {
		super();
	}

	public LayoutFrame(String title) {
		super(title);
	}

	public LayoutFrame(String code, String title) {
		super(code, title);
	}

	public LayoutFrame(String code, String title, Integer width, Integer height) {
		super(code, title, width, height);
	}

	protected void defaultInit(String code, String title, Integer width,
			Integer height) {
		super.defaultInit(code, title, width, height);
		initLayout();
	}

	private void initLayout() {
		this.setLayout(new GridBagLayout());
		gridBagConstraints = new GridBagConstraints();
		componentMap = new HashMap<String, Component>();
		insets = new Insets(0, 0, 0, 0);
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
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weightx = width * 10;
		gridBagConstraints.weighty = height * 10;
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
		setVisible(true);
	}

	public void setInsets(int top, int left, int bottom, int right) {
		insets = new Insets(top, left, bottom, right);
	}

}
