package com.framework.swing.components.dialogs;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import com.framework.common.utils.ThreadUtil;
import com.framework.swing.beans.ProgressBean;

public class ProgressBarDialog extends JDialog {

	private static final long serialVersionUID = 6380840286995517442L;
	private JProgressBar progressBar;
	private JLabel textLabel;

	public ProgressBarDialog(JFrame frame) {
		super(frame);
		init();
		setLocationRelativeTo(frame);
	}

	public ProgressBarDialog(JDialog frame) {
		super(frame);
		init();
		setLocationRelativeTo(frame);
	}

	private void init() {
		this.setTitle("操作中，请稍后...");
		this.setModal(true);
		this.setSize(new Dimension(500, 150));
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setUndecorated(true);
		JPanel mainPanel = new JPanel();
		this.add(mainPanel);
		mainPanel.setLayout(null);
		mainPanel.setBackground(Color.WHITE);
		progressBar = new JProgressBar();
		progressBar.setBounds(50, 50, 400, 20);
		textLabel = new JLabel("", SwingConstants.LEFT);
		textLabel.setBounds(50, 20, 400, 20);
		textLabel.setPreferredSize(new Dimension(400, 20));
		mainPanel.add(textLabel);
		mainPanel.add(progressBar);
	}

	public void numberStart(ProgressBean bean) {

		new Thread() {
			@Override
			public void run() {
				if (bean.getNumber() == null || bean.getMax() == null || bean.getMax() < bean.getNumber()) {
					setVisible(false);
				}
				while (!bean.getClose()) {
					progressBar.setMaximum(bean.getMax());
					progressBar.setValue(bean.getNumber() > bean.getMax() ? bean.getMax() : bean.getNumber());
					textLabel.setText(bean.getMessage());
					ThreadUtil.sleep(200);
				}
				setVisible(false);
			}
		}.start();
	}

	public void loopStart(ProgressBean bean) {
		new Thread() {
			@Override
			public void run() {
				int number = 0;
				progressBar.setMaximum(100);
				while (!bean.getClose()) {
					number = number > 100 ? 0 : ++number;
					progressBar.setValue(number);
					textLabel.setText(bean.getMessage());
					ThreadUtil.sleep(200);
				}
				setVisible(false);
			}
		}.start();
	}

}
