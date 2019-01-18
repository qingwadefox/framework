package org.qingfox.framework.swing.components;

import java.awt.Component;

public interface ILayoutComponent {

	public void addComponent(Component component);

	public void addComponent(Component component, int x, int y);

	public void addComponent(Component component, int x, int y, int width,
			int height);

	public Component getComponent(int x, int y);

	public void setInsets(int top, int left, int bottom, int right);

}
