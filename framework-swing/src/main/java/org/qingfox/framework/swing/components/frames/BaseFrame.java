package org.qingfox.framework.swing.components.frames;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import org.qingfox.framework.common.utils.RandomUtil;
import org.qingfox.framework.swing.beans.ProgressBean;
import org.qingfox.framework.swing.components.IComponent;
import org.qingfox.framework.swing.components.dialogs.ProgressBarDialog;
import org.qingfox.framework.swing.factory.ComponentFactory;

public class BaseFrame extends JFrame implements IComponent {

	private static final long serialVersionUID = -2680847374131811784L;
	private final static Integer DEFAULT_WIDTH = 800;
	private final static Integer DEFAULT_HEIGHT = 600;
	private String code;
	private ProgressBarDialog progressBar;

	public BaseFrame() {
		this.defaultInit(null, null, null, null);
	}

	public BaseFrame(String title) {
		this.defaultInit(null, title, null, null);
	}

	public BaseFrame(String code, String title) {
		this.defaultInit(code, title, null, null);
	}

	public BaseFrame(String code, String title, Integer width, Integer height) {
		this.defaultInit(code, title, width, height);
	}

	protected void defaultInit(String code, String title, Integer width, Integer height) {

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
		this.setSize(width, height);

		ComponentFactory.init(this);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(true);
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
