package org.qingfox.framework.swing.components.dialogs;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.qingfox.framework.swing.beans.ProgressBean;

public class BaseDialog extends JDialog {
	private static final long serialVersionUID = 7434901577876636628L;
	private ProgressBarDialog progressBar;

	public BaseDialog(JFrame frame) {
		super(frame);
	}

	public void showNumberProgress(ProgressBean bean) {
		if (progressBar == null) {
			progressBar = new ProgressBarDialog(this);
		}
		progressBar.numberStart(bean);
		progressBar.setVisible(true);
	}

	public void showLoopProcess(ProgressBean bean) {
		if (progressBar == null) {
			progressBar = new ProgressBarDialog(this);
		}
		progressBar.loopStart(bean);
		progressBar.setVisible(true);
	}

	public void closeProgressBarDialog() {
		if (progressBar != null) {
			progressBar.setVisible(false);
		}
	}

	public void showErrorMessage(String message) {
		showErrorMessage("操作失败", message);
	}

	public void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
}
