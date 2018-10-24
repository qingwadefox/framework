/**
 * 
 */
package com.framework.test.pdd.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.framework.common.beans.Result;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.ThreadUtil;
import com.framework.swing.beans.ProgressBean;
import com.framework.swing.components.dialogs.BaseDialog;
import com.framework.swing.components.frames.BaseFrame;
import com.framework.test.pdd.entity.AccountEntity;
import com.framework.test.pdd.service.inf.ILoginService;
import com.framework.test.pdd.service.listeners.LoginListener;

/**
 * @author zhengwei
 *
 */
public class LoginDialog extends BaseDialog implements LoginListener, ActionListener {

	private static final long serialVersionUID = -3271382479502542100L;

	private static final ILogger logger = LoggerFactory.getLogger(JDialog.class);

	private JButton loginBtn;
	private JButton refCodeBtn;
	private AccountEntity account;
	private ILoginService loginService;
	private BaseFrame frame;

	private JPanel qrCodePanel;
	private JLabel qrCodeLabel;

	private JPanel codePanel;
	private JLabel codeLabel;
	private JTextField codeTextField;

	public LoginDialog(BaseFrame frame) {
		super(frame);
		this.frame = frame;
		this.setTitle("登录窗口");
		this.setModal(true);
		this.setSize(new Dimension(300, 400));
		this.setResizable(false);
		this.setUndecorated(true);
		this.setLocationRelativeTo(frame);
		this.setLayout(new BorderLayout(10, 10));

		qrCodePanel = new JPanel();
		qrCodeLabel = new JLabel();
		qrCodeLabel.setHorizontalAlignment(JLabel.CENTER);
		qrCodePanel.add(qrCodeLabel);
		qrCodePanel.setPreferredSize(new Dimension(250, 250));
		this.add(qrCodePanel, BorderLayout.CENTER);

		codePanel = new JPanel();
		codeLabel = new JLabel();
		JLabel spaceLabel = new JLabel();
		spaceLabel.setPreferredSize(new Dimension(200, 100));
		codePanel.add(spaceLabel);
		codeLabel.setHorizontalAlignment(JLabel.CENTER);
		codeLabel.setPreferredSize(new Dimension(200, 50));
		codePanel.add(codeLabel);
		codeTextField = new JTextField();
		codeTextField.setPreferredSize(new Dimension(200, 25));
		codePanel.add(codeTextField);
		this.add(codePanel, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		loginBtn = new JButton("登录");
		loginBtn.setPreferredSize(new Dimension(80, 25));
		loginBtn.addActionListener(this);
		refCodeBtn = new JButton("刷新二维码");
		refCodeBtn.setPreferredSize(new Dimension(80, 25));
		refCodeBtn.addActionListener(this);
		btnPanel.add(loginBtn);
		btnPanel.add(refCodeBtn);
		this.add(btnPanel, BorderLayout.SOUTH);
	}

	public void setLoginInfo(AccountEntity account, ILoginService loginService) {
		this.account = account;
		this.loginService = loginService;
		this.loginService.addLoginListener(this);
		if (this.account.getType().equals(AccountEntity.TYPE_SELL)) {
			this.codeTextField.setText("");
			this.remove(qrCodePanel);
			this.add(codePanel, BorderLayout.CENTER);
		} else {
			this.remove(codePanel);
			this.add(qrCodePanel, BorderLayout.CENTER);
		}
		this.revalidate();
		try {
			this.loginService.refreshCode();
		} catch (Exception e) {
			logger.error(e, "刷新二维码失败");
			this.setVisible(false);
			frame.showErrorMessage("刷新二维码失败");
		}
	}

	@Override
	public void onLogin(Object source) {
		account.setStateDate(new Date());
		this.loginService.removeLoginListener(this);
		this.closeProgressBarDialog();
		this.setVisible(false);
	}

	@Override
	public void onCodeChange(Object source, File codeFile) {
		if (codeFile == null || !codeFile.exists()) {
			return;
		}
		ImageIcon image = new ImageIcon(codeFile.getPath());

		switch (account.getType()) {
			case AccountEntity.TYPE_COMM :
				image.setImage(image.getImage().getScaledInstance(240, 240, Image.SCALE_DEFAULT));
				qrCodeLabel.setIcon(image);
				break;
			case AccountEntity.TYPE_BUY :
				image.setImage(image.getImage().getScaledInstance(240, 240, Image.SCALE_DEFAULT));
				qrCodeLabel.setIcon(image);
				break;
			case AccountEntity.TYPE_SELL :
				image.setImage(image.getImage().getScaledInstance(200, 50, Image.SCALE_DEFAULT));
				codeLabel.setIcon(image);
				break;
		}
	}

	@Override
	public void onLogout(Object sourcer) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(refCodeBtn)) {
			ProgressBean progress = new ProgressBean();
			Result<Void> result = new Result<Void>();
			new Thread() {
				@Override
				public void run() {
					ThreadUtil.sleep(1000);
					try {
						loginService.refreshCode();
					} catch (Exception e1) {
						result.setSuccess(false);
						result.setMessage("刷新二维码失败");
						logger.error(e, result.getMessage());
					}
					progress.setClose(true);
				}
			}.start();
			showLoopProcess(progress);
			if (!result.getSuccess()) {
				showErrorMessage(result.getMessage());
			}
		} else if (e.getSource().equals(loginBtn)) {
			ProgressBean progress = new ProgressBean();
			Result<Void> result = new Result<Void>();
			new Thread() {
				@Override
				public void run() {
					ThreadUtil.sleep(1000);

					try {
						String code = account.getType().equals(AccountEntity.TYPE_SELL) ? codeTextField.getText() : null;
						loginService.login(account.getUsername(), account.getPassword(), code);
					} catch (Exception e) {
						result.setSuccess(false);
						result.setMessage("登录失败");
						logger.error(e, result.getMessage());
					}
					progress.setClose(true);
				}
			}.start();
			showLoopProcess(progress);
			if (!result.getSuccess()) {
				showErrorMessage(result.getMessage());
			}
		}

	}

}
