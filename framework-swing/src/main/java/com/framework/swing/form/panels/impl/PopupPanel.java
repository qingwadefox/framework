package com.framework.swing.form.panels.impl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class PopupPanel extends JPanel implements MouseListener, FocusListener,
		AncestorListener {

	private static final long serialVersionUID = 666555336614626460L;

	private boolean hide = true;
	private Popup pop;
	private JComponent cmp;

	public boolean isHide() {
		return hide;
	}

	public PopupPanel(JComponent cmp) {
		this.cmp = cmp;
		if (cmp != null) {
			cmp.addMouseListener(this);
			cmp.addFocusListener(this);
			cmp.addAncestorListener(this);
		}
	}

	public JComponent getCmp() {
		return cmp;
	}

	// 隐藏日期选择面板
	public void hidePanel() {
		if (pop != null) {
			hide = true;
			pop.hide();
			pop = null;
		}
	}

	// 显示日期选择面板
	public void showPanel() {
		if (hide) {
			Point show = new Point(0, cmp.getHeight());
			SwingUtilities.convertPointToScreen(show, cmp);
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			int x = show.x;
			int y = show.y;
			if (x < 0) {
				x = 0;
			}
			if (x > size.width - 295) {
				x = size.width - 295;
			}
			if (y < size.height - 170) {
			} else {
				y -= 188;
			}
			pop = PopupFactory.getSharedInstance().getPopup(cmp, this, x, y);
			pop.show();
			hide = false;
		}

	}

	
	public void mouseClicked(MouseEvent e) {
		showPanel();

	}

	
	public void mousePressed(MouseEvent e) {

	}

	
	public void mouseReleased(MouseEvent e) {
	}

	
	public void mouseEntered(MouseEvent e) {

	}

	
	public void mouseExited(MouseEvent e) {

	}

	
	public void focusGained(FocusEvent e) {
		this.showPanel();

	}

	
	public void focusLost(FocusEvent e) {
		this.hidePanel();

	}

	
	public void ancestorAdded(AncestorEvent event) {

	}

	
	public void ancestorRemoved(AncestorEvent event) {

	}

	
	public void ancestorMoved(AncestorEvent event) {
//		this.hidePanel();
	}

}
