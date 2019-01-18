package org.qingfox.framework.swing.components.labels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicLabelUI;

public class EllipseLabel extends JLabel {

	private static final long serialVersionUID = 3319298181143884789L;

	public EllipseLabel() {
		super();
		init();
	}

	public EllipseLabel(String arg0, int arg1) {
		super(arg0, arg1);
		init();
	}

	private void init() {
		this.setUI(new BasicLabelUI() {
			@Override
			public void update(Graphics g, JComponent c) {
				if (c.isOpaque()) {
					Graphics2D g2 = (Graphics2D) g;
					g2.setColor(c.getBackground());
					RoundRectangle2D rectRound = new RoundRectangle2D.Double(0,
							0, c.getWidth() - 1, c.getHeight() - 1, 5, 5);
					g2.fill(rectRound);
				}
				paint(g, c);
			}
		});
	}

}
