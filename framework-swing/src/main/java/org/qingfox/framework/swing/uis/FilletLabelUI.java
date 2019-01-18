package org.qingfox.framework.swing.uis;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicLabelUI;


public class FilletLabelUI extends BasicLabelUI {

	public void paint(Graphics g, JComponent c) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.BLUE);
//		c.setBorder(new FilletLineBorder(Color.LIGHT_GRAY, 1, true));
//		g2.drawRoundRect(0, 0, c.getWidth() - 10, c.getHeight() - 10, 20, 20);
//		g2.fillRoundRect(0, 0, c.getWidth() - 10, c.getHeight() - 10, 20, 20);
		// g2.set
		super.paint(g, c);

	}
}
