package com.framework.swing.uis;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;

public class BELabelUI extends BasicLabelUI {

	@Override
	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		// AlphaComposite alphaComposite = AlphaComposite.getInstance(
		// AlphaComposite.SRC_OVER, 0.42f);
		// g2.setComposite(alphaComposite);// 透明度
		super.paint(g2, c);

	}

}
