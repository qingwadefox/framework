/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package org.qingfox.framework.test.pdd.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.qingfox.framework.test.pdd.constant.PathConstant;
import org.qingfox.framework.test.pdd.entity.AccountEntity;
import org.qingfox.framework.test.pdd.entity.ChatEntity;
import org.qingfox.framework.test.pdd.entity.GoodsEntity;
import org.qingfox.framework.test.pdd.entity.OrderEntity;
import org.qingfox.framework.test.pdd.service.AmmDatamineService;
import org.qingfox.framework.test.pdd.service.AmmService;
import org.qingfox.framework.test.pdd.service.PddService;
import org.qingfox.framework.test.pdd.service.TaobaoService;
import org.qingfox.framework.test.pdd.service.inf.IBuyService;
import org.qingfox.framework.test.pdd.service.inf.ICommissionService;
import org.qingfox.framework.test.pdd.service.inf.IDatamineService;
import org.qingfox.framework.test.pdd.service.inf.ILoginService;
import org.qingfox.framework.test.pdd.service.inf.ISellService;
import org.qingfox.framework.test.pdd.service.listeners.ChatListener;
import org.qingfox.framework.test.pdd.service.listeners.LoginListener;
import org.qingfox.framework.test.pdd.swing.dialog.LoginDialog;
import org.qingfox.framework.test.pdd.threads.SellServiceTask;
import org.qingfox.framework.test.pdd.threads.listener.ISellServiceTaskListener;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.beans.Field;
import com.framework.common.beans.Result;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.PropertiesUtil;
import com.framework.common.utils.ThreadUtil;
import com.framework.swing.beans.ProgressBean;
import com.framework.swing.components.frames.BaseFrame;
import com.framework.swing.components.panels.LayoutPanel;
import com.framework.swing.components.table.TablePanel;
import com.framework.swing.form.panels.fields.IField;
import com.framework.swing.form.panels.fields.impl.ComboField;
import com.framework.swing.form.panels.fields.impl.ListField;
import com.framework.swing.form.panels.fields.impl.TextField;
import com.framework.swing.form.panels.impl.ComboFieldPanel;
import com.framework.swing.form.panels.impl.TextComboFieldPanel;
import com.framework.swing.inf.IColumnRender;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2017年12月26日 @功能说明：
 * 
 */
public class SwingMain extends BaseFrame implements ActionListener, ItemListener, MouseListener, ISellServiceTaskListener, LoginListener, ListSelectionListener, ChatListener {

	private static final long serialVersionUID = -1955193756615822409L;

	private static final ILogger logger = LoggerFactory.getLogger(SwingMain.class);

	private Map<String, ISellService> sellServiceMap;

	private Map<String, IBuyService> buyServiceMap;

	private Map<String, ICommissionService> commissionSericeMap;

	private Map<String, SellServiceTask> sellServiceTaskMap;

	private IDatamineService ammDatamineService;

	private LoginDialog loginDialog;

	/***************************************************************************************
	 * 操作主界面
	 ***************************************************************************************/
	private JPanel operationP;

	/***************************************************************************************
	 * 账号管理
	 ***************************************************************************************/
	private JTabbedPane accountTabPanel;
	private TablePanel<AccountEntity> commAccountTable;
	private TablePanel<AccountEntity> sellAccountTable;
	private TablePanel<AccountEntity> buyAccountTable;
	private JButton accountLoginBtn;
	private JButton accountLogoutBtn;

	/***************************************************************************************
	 * 任务界面
	 ***************************************************************************************/
	private TextComboFieldPanel taskCommAccountCombo;
	private JButton taskStartBtn;
	private JButton taskStopBtn;
	private TablePanel<Map<String, String>> taskSellAccountTable;

	/***************************************************************************************
	 * 订单界面
	 ***************************************************************************************/
	private TextComboFieldPanel sellOrderAccountCombo;
	private TextComboFieldPanel buyOrderAccountCombo;
	public JButton orderSearchBtn;
	public JButton orderBuyBtn;
	public JButton orderShipBtn;
	public JButton orderGroupBtn;
	private TablePanel<OrderEntity> orderListTable;

	/***************************************************************************************
	 * 聊天窗口界面
	 ***************************************************************************************/
	public JTextPane chatTextPane;
	public JScrollPane chatTextSPane;
	public JButton chatSendBtn;
	public JTextArea chatSendText;
	public ListField<ChatEntity> chatUserList;

	@Override
	protected void defaultInit(String code, String title, Integer width, Integer height) {
		super.defaultInit(code, title, 1100, 600);
		loginDialog = new LoginDialog(this);
		initOperatorPanel();
		Field<String> field = new Field<>();
		initConfig();
		setResizable(false);
		setVisible(true);
	}

	/**
	 * 初始化配置 .
	 * 
	 * @author Administrator 2017年12月29日 Administrator
	 */
	private void initConfig() {
		sellServiceMap = new HashMap<>();
		buyServiceMap = new HashMap<>();
		commissionSericeMap = new HashMap<>();
		sellServiceTaskMap = new HashMap<>();
		try {
			Properties config = PropertiesUtil.loadProperties(new File(PathConstant.DIR_ROOT + File.separator + PathConstant.DIR_CONFIG + File.separator + "accounts.properties"));

			String[] commUsernames = config.getProperty("comm.usernames").split(",");
			String[] commPasswords = config.getProperty("comm.passwords").split(",");
			List<AccountEntity> commAccountList = new ArrayList<AccountEntity>();
			for (int i = 0; i < commUsernames.length; i++) {
				AccountEntity entity = new AccountEntity();
				entity.setUsername(commUsernames[i]);
				entity.setPassword(commPasswords[i]);
				entity.setType(AccountEntity.TYPE_COMM);
				commAccountList.add(entity);
			}
			this.commAccountTable.setDatas(commAccountList);

			String[] buyUsernames = config.getProperty("buy.usernames").split(",");
			String[] buyPasswords = config.getProperty("buy.passwords").split(",");
			List<AccountEntity> buyAccountList = new ArrayList<AccountEntity>();
			for (int i = 0; i < buyUsernames.length; i++) {
				AccountEntity entity = new AccountEntity();
				entity.setUsername(buyUsernames[i]);
				entity.setPassword(buyPasswords[i]);
				entity.setType(AccountEntity.TYPE_BUY);
				buyAccountList.add(entity);
			}
			this.buyAccountTable.setDatas(buyAccountList);

			String[] sellUsernames = config.getProperty("sell.usernames").split(",");
			String[] sellPasswords = config.getProperty("sell.passwords").split(",");
			List<AccountEntity> sellAccountList = new ArrayList<AccountEntity>();
			for (int i = 0; i < sellUsernames.length; i++) {
				AccountEntity entity = new AccountEntity();
				entity.setUsername(sellUsernames[i]);
				entity.setPassword(sellPasswords[i]);
				entity.setType(AccountEntity.TYPE_SELL);
				sellAccountList.add(entity);
			}
			this.sellAccountTable.setDatas(sellAccountList);
			ammDatamineService = new AmmDatamineService();
			ammDatamineService.login(null, null, null);

		} catch (Exception e) {
			logger.error(e, "读取配置文件失败");
			JOptionPane.showMessageDialog(this, "读取配置文件失败", "初始化失败", JOptionPane.ERROR_MESSAGE);
			System.exit(ERROR);
		}
	}

	/**
	 * 初始化操作面板 .
	 * 
	 * @author Administrator 2017年12月29日 Administrator
	 */
	private void initOperatorPanel() {
		operationP = new JPanel();
		operationP.setLayout(new BorderLayout());
		JTabbedPane operationTP = new JTabbedPane();
		operationP.add(operationTP, BorderLayout.CENTER);

		/*********************************************************************************
		 * 账号管理界面
		 *********************************************************************************/
		IColumnRender<Boolean> accountLoginRender = new IColumnRender<Boolean>() {
			@Override
			public Object render(Boolean value) {
				return value == null ? "否" : value ? "是" : "否";
			}
		};
		IColumnRender<Date> accountStateDateRander = new IColumnRender<Date>() {
			@Override
			public Object render(Date value) {
				if (value == null) {
					return "";
				} else {
					return DateUtil.format(value);
				}
			}
		};
		JPanel accountPanel = new JPanel();
		accountPanel.setLayout(new BorderLayout());
		JPanel accountBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		accountLoginBtn = new JButton("登录");
		accountLoginBtn.setPreferredSize(new Dimension(80, 25));
		accountLoginBtn.addActionListener(this);
		accountBtnPanel.add(accountLoginBtn);
		accountLogoutBtn = new JButton("注销");
		accountLogoutBtn.setPreferredSize(new Dimension(80, 25));
		accountBtnPanel.add(accountLogoutBtn);
		accountPanel.add(accountBtnPanel, BorderLayout.NORTH);

		accountTabPanel = new JTabbedPane(JTabbedPane.LEFT);
		commAccountTable = new TablePanel<AccountEntity>();
		commAccountTable.addColumnBean("username", "用户名");
		commAccountTable.setColumnEditor("username", new TextField());
		commAccountTable.addColumnBean("password", "密码");
		commAccountTable.setColumnEditor("password", new TextField());
		commAccountTable.addColumnBean("login", "登录");
		commAccountTable.addColumnRender("login", accountLoginRender);
		commAccountTable.addColumnBean("stateDate", "状态时间");
		commAccountTable.addColumnRender("stateDate", accountStateDateRander);
		accountTabPanel.add("推广账号管理", commAccountTable);

		buyAccountTable = new TablePanel<AccountEntity>();
		buyAccountTable.addColumnBean("username", "用户名");
		buyAccountTable.setColumnEditor("username", new TextField());
		buyAccountTable.addColumnBean("password", "密码");
		buyAccountTable.setColumnEditor("password", new TextField());
		buyAccountTable.addColumnBean("login", "状态");
		buyAccountTable.addColumnRender("login", accountLoginRender);
		buyAccountTable.addColumnBean("stateDate", "状态时间");
		buyAccountTable.addColumnRender("stateDate", accountStateDateRander);
		accountTabPanel.add("购买账号管理", buyAccountTable);

		sellAccountTable = new TablePanel<AccountEntity>();
		sellAccountTable.addColumnBean("username", "用户名");
		sellAccountTable.setColumnEditor("username", new TextField());
		sellAccountTable.addColumnBean("password", "密码");
		sellAccountTable.setColumnEditor("password", new TextField());
		sellAccountTable.addColumnBean("login", "登录");
		sellAccountTable.addColumnRender("login", accountLoginRender);
		sellAccountTable.addColumnBean("stateDate", "状态时间");
		sellAccountTable.addColumnRender("stateDate", accountStateDateRander);
		accountTabPanel.add("销售账号管理", sellAccountTable);
		accountPanel.add(accountTabPanel, BorderLayout.CENTER);

		operationTP.addTab("账号管理", accountPanel);

		/*********************************************************************************
		 * 发布商品面板内容
		 *********************************************************************************/
		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new BorderLayout(0, 0));

		JPanel taskOperatorPanel = new JPanel();
		taskOperatorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		taskCommAccountCombo = new TextComboFieldPanel("commAccountCombo", "请选择推广账户");
		taskCommAccountCombo.setPreferredSize(new Dimension(300, 25));
		taskOperatorPanel.add(taskCommAccountCombo);

		taskStartBtn = new JButton("开始任务");
		taskStartBtn.setPreferredSize(new Dimension(80, 25));
		taskStartBtn.addActionListener(this);
		taskOperatorPanel.add(taskStartBtn);

		taskStopBtn = new JButton("停止任务");
		taskStopBtn.setPreferredSize(new Dimension(80, 25));
		taskStopBtn.addActionListener(this);
		taskOperatorPanel.add(taskStopBtn);

		taskPanel.add(taskOperatorPanel, BorderLayout.NORTH);

		// 表格
		taskSellAccountTable = new TablePanel<Map<String, String>>();
		taskSellAccountTable.addColumnBean("username", "账号");
		taskSellAccountTable.addColumnBean("checkState", "检测状态");
		taskSellAccountTable.addColumnBean("checkSuccess", "下架成功");
		taskSellAccountTable.addColumnBean("checkFailure", "下架失败");
		taskSellAccountTable.addColumnBean("publicState", "发布状态");
		taskSellAccountTable.addColumnBean("publicSuccess", "发布成功");
		taskSellAccountTable.addColumnBean("publicFailure", "发布失败");
		taskSellAccountTable.addColumnBean("lastTaskTime", "更新时间");
		taskSellAccountTable.addColumnRender("checkState", new IColumnRender<String>() {
			@Override
			public Object render(String value) {
				switch (Integer.parseInt(value)) {
				case 0:
					return "未检测";
				case 1:
					return "检测中";
				case 2:
					return "检测成功";
				case 3:
					return "检测失败";
				}
				return "";
			}
		});

		taskSellAccountTable.addColumnRender("publicState", new IColumnRender<String>() {
			@Override
			public Object render(String value) {
				switch (Integer.parseInt(value)) {
				case 0:
					return "未发布";
				case 1:
					return "发布中";
				case 2:
					return "发布成功";
				case 3:
					return "发布失败";
				}
				return "";
			}
		});
		taskSellAccountTable.addColumnRender("lastTaskTime", new IColumnRender<String>() {
			@Override
			public Object render(String value) {
				if (StringUtils.isNotEmpty(value)) {
					return DateUtil.format(Long.parseLong(value));
				} else {
					return "";
				}
			}
		});
		taskPanel.add(taskSellAccountTable, BorderLayout.CENTER);
		operationTP.addTab("任务列表", taskPanel);

		/*********************************************************************************
		 * 订单面板
		 *********************************************************************************/
		JPanel orderPanel = new JPanel();
		orderPanel.setLayout(new BorderLayout(0, 0));

		// 操作按钮
		JPanel orderOperatorPanel = new JPanel();
		orderOperatorPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		sellOrderAccountCombo = new TextComboFieldPanel("sellOrderAccountCombo", "请选择销售账户");
		sellOrderAccountCombo.setPreferredSize(new Dimension(300, 25));
		orderOperatorPanel.add(sellOrderAccountCombo);

		buyOrderAccountCombo = new TextComboFieldPanel("buyOrderAccountCombo", "请选择购买账户");
		buyOrderAccountCombo.setPreferredSize(new Dimension(300, 25));
		orderOperatorPanel.add(buyOrderAccountCombo);

		orderSearchBtn = new JButton("查询订单");
		orderSearchBtn.setPreferredSize(new Dimension(80, 25));
		orderSearchBtn.addActionListener(this);
		orderOperatorPanel.add(orderSearchBtn);

		orderBuyBtn = new JButton("购买订单");
		orderBuyBtn.setPreferredSize(new Dimension(80, 25));
		orderBuyBtn.addActionListener(this);
		orderOperatorPanel.add(orderBuyBtn);

		orderShipBtn = new JButton("卖单发货");
		orderShipBtn.setPreferredSize(new Dimension(80, 25));
		orderShipBtn.addActionListener(this);
		orderOperatorPanel.add(orderShipBtn);

		orderGroupBtn = new JButton("未团拼单");
		orderGroupBtn.setPreferredSize(new Dimension(80, 25));
		orderGroupBtn.addActionListener(this);
		orderOperatorPanel.add(orderGroupBtn);

		orderPanel.add(orderOperatorPanel, BorderLayout.NORTH);

		// 表格
		orderListTable = new TablePanel<OrderEntity>();
		orderListTable.addColumnBean("sellOrderId", "卖单ID");
		orderListTable.addColumnBean("sellReceiveName", "卖单买家");
		orderListTable.addColumnBean("sellAmount", "卖单价格");
		orderListTable.addColumnBean("sellOrderState", "卖单状态");
		orderListTable.addColumnBean("sellGoodsSpec", "卖单SKU信息");
		orderListTable.addColumnBean("buyOrderId", "买单ID");
		orderListTable.addColumnBean("buyAmount", "买单价格");
		orderListTable.addColumnBean("buyOrderState", "买单状态");
		orderListTable.addColumnRender("sellAmount", new IColumnRender<String>() {
			@Override
			public Object render(String value) {
				return Float.parseFloat(value.toString()) / 100;
			}
		});
		orderListTable.addColumnRender("sellOrderState", new IColumnRender<Integer>() {
			@Override
			public Object render(Integer value) {
				return OrderEntity.sellOrderStateToText(value);
			}
		});

		orderListTable.addColumnRender("sellOrderState", new IColumnRender<Integer>() {
			@Override
			public Object render(Integer value) {
				return OrderEntity.sellOrderStateToText(value);
			}
		});

		orderListTable.addColumnRender("buyOrderState", new IColumnRender<Integer>() {
			@Override
			public Object render(Integer value) {
				return OrderEntity.buyOrderStateToText(value);
			}
		});
		orderListTable.getTable().addMouseListener(this);
		orderPanel.add(orderListTable, BorderLayout.CENTER);

		operationTP.addTab("订单查询", orderPanel);

		/*********************************************************************************
		 * 聊天面板
		 *********************************************************************************/
		JPanel chatPanel = new JPanel();
		chatPanel.setLayout(new BorderLayout(5, 5));
		chatUserList = new ListField<ChatEntity>();
		chatUserList.addListSelectionListener(this);
		chatUserList.setPreferredSize(new Dimension(200, 100));
		chatPanel.add(chatUserList, BorderLayout.WEST);
		chatTextPane = new JTextPane();
		chatTextPane.setContentType("text/html");
		chatTextPane.setBorder(null);
		chatTextSPane = new JScrollPane(chatTextPane);
		chatPanel.add(chatTextSPane, BorderLayout.CENTER);

		JPanel chatSendPanel = new JPanel();
		chatSendPanel.setLayout(new BorderLayout(5, 5));
		this.chatSendText = new JTextArea();
		this.chatSendText.setBorder(null);
		JScrollPane chatSendSp = new JScrollPane(this.chatSendText);
		chatSendPanel.add(chatSendSp, BorderLayout.CENTER);
		this.chatSendBtn = new JButton();
		chatSendBtn.setPreferredSize(new Dimension(50, 50));
		chatSendBtn.addActionListener(this);
		chatSendPanel.add(chatSendBtn, BorderLayout.EAST);
		chatPanel.add(chatSendPanel, BorderLayout.SOUTH);
		operationTP.addTab("聊天窗口", chatPanel);
		add(operationTP);
	}

	/**
	 * 商品发布事件 .
	 * 
	 * @author Administrator 2017年12月29日 Administrator
	 */
	private void taskStartClick() {
		Field<String> taskCommAccountSelectField = taskCommAccountCombo.getComponent().getSelectItem();
		if (taskCommAccountSelectField == null || commissionSericeMap.get(taskCommAccountSelectField.getCode()) == null) {
			showErrorMessage("请选择推广服务");
			return;
		}

		Map<String, String> taskSellAccountSelectMap = taskSellAccountTable.getSelectData();
		if (taskSellAccountSelectMap == null || sellServiceTaskMap.get(taskSellAccountSelectMap.get("id")) == null) {
			showErrorMessage("请选择销售服务");
			return;
		}

		SellServiceTask task = sellServiceTaskMap.get(taskSellAccountSelectMap.get("id"));
		task.setCommissionService(commissionSericeMap.get(taskCommAccountSelectField.getCode()));
		task.setDatamineService(ammDatamineService);
		task.proceed();
	}
	
	private void taskStopClick() {
		Field<String> taskCommAccountSelectField = taskCommAccountCombo.getComponent().getSelectItem();
		if (taskCommAccountSelectField == null || commissionSericeMap.get(taskCommAccountSelectField.getCode()) == null) {
			showErrorMessage("请选择推广服务");
			return;
		}

		Map<String, String> taskSellAccountSelectMap = taskSellAccountTable.getSelectData();
		if (taskSellAccountSelectMap == null || sellServiceTaskMap.get(taskSellAccountSelectMap.get("id")) == null) {
			showErrorMessage("请选择销售服务");
			return;
		}

		SellServiceTask task = sellServiceTaskMap.get(taskSellAccountSelectMap.get("id"));
		task.setCommissionService(commissionSericeMap.get(taskCommAccountSelectField.getCode()));
		task.setDatamineService(ammDatamineService);
		task.pause();
	}

	/**
	 * 获取当前发布任务 .
	 * 
	 * @return
	 * @author Administrator 2018年1月2日 Administrator
	 */
	// private List<SellServiceTask> getSellServiceTask() {
	// List<SellServiceTask> taskList = new ArrayList<SellServiceTask>();
	// String sellId = (String)
	// selPublicAccountCombo.getComponent().getSelectedItem();
	// if (sellId.equals("-1")) {
	// Field<AccountEntity>[] fields =
	// selPublicAccountCombo.getComponent().getFields();
	// for (int i = 1; i < fields.length; i++) {
	// SellServiceTask task = fields[i].getValue().getSellServiceTask();
	// if (task == null) {
	// task = new SellServiceTask((ISellService)
	// fields[i].getValue().getService());
	// task.start();
	// fields[i].getValue().setSellServiceTask(task);
	// }
	// taskList.add(task);
	// }
	// } else {
	// Field<AccountEntity> field =
	// selPublicAccountCombo.getComponent().getSelectItem();
	// SellServiceTask task = field.getValue().getSellServiceTask();
	// if (task == null) {
	// task = new SellServiceTask((ISellService) field.getValue().getService());
	// task.start();
	// field.getValue().setSellServiceTask(task);
	// }
	// taskList.add(task);
	// }
	// return taskList;
	// }

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(orderListTable.getTable())) {
			if (e.getClickCount() == 2) {
				OrderEntity order = orderListTable.getSelectData();
				try {
					Runtime.getRuntime().exec("cmd.exe /c start http://s.click.taobao.com/" + order.getBuyUrl());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(taskStartBtn)) {
			taskStartClick();
		}else if(e.getSource().equals(taskStopBtn)) {
			taskStopClick();
		} else if (e.getSource().equals(orderSearchBtn)) {
			orderSearchClick();
		} else if (e.getSource().equals(orderBuyBtn)) {
			orderBuyClick();
		} else if (e.getSource().equals(orderShipBtn)) {
			orderShipClick();
		} else if (e.getSource().equals(orderGroupBtn)) {
			orderGroupClick();
		} else if (e.getSource().equals(chatSendBtn)) {
			chatSendClick();
		} else if (e.getSource().equals(this.accountLoginBtn)) {
			accountLoginClick();
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
	}

	private void accountLoginClick() {
		Result<Void> result = new Result<Void>();
		ProgressBean progress = new ProgressBean();
		LoginListener _this = this;

		new Thread() {
			@Override
			public void run() {
				ThreadUtil.sleep(1000);
				AccountEntity account = null;
				switch (accountTabPanel.getSelectedIndex()) {
				case 0:
					account = commAccountTable.getSelectData();
					break;
				case 1:
					account = buyAccountTable.getSelectData();
					break;
				case 2:
					account = sellAccountTable.getSelectData();
					break;
				}
				if (account == null) {
					result.setSuccess(false);
					result.setMessage("请选择账户");
					progress.setClose(true);
					return;
				}

				ILoginService loginService = null;
				if (StringUtils.isNotEmpty(account.getId())) {
					switch (account.getType()) {
					case AccountEntity.TYPE_COMM:
						loginService = commissionSericeMap.get(account.getId());
						break;
					case AccountEntity.TYPE_BUY:
						loginService = buyServiceMap.get(account.getId());
						break;
					case AccountEntity.TYPE_SELL:
						loginService = sellServiceMap.get(account.getId());
						break;
					}
					if (loginService != null && loginService.isLogin()) {
						result.setSuccess(false);
						result.setMessage("用户已登录");
						progress.setClose(true);
						return;
					}
				}

				if (loginService == null) {
					try {
						switch (account.getType()) {
						case AccountEntity.TYPE_COMM:
							loginService = new AmmService();
							commissionSericeMap.put(loginService.getId(), (ICommissionService) loginService);
							break;
						case AccountEntity.TYPE_BUY:
							loginService = new TaobaoService();
							buyServiceMap.put(loginService.getId(), (IBuyService) loginService);
							break;
						case AccountEntity.TYPE_SELL:
							loginService = new PddService();
							sellServiceMap.put(loginService.getId(), (ISellService) loginService);
							break;
						}
					} catch (Exception e) {
						logger.error(e, "初始化服务失败");
						result.setSuccess(false);
						result.setMessage("初始化服务失败");
						progress.setClose(true);
						return;
					}
				}
				if (loginService == null) {
					result.setSuccess(false);
					result.setMessage("初始化服务失败");
					progress.setClose(true);
					return;
				}
				account.setId(loginService.getId());

				loginService.addLoginListener(_this);

				boolean loginSuccess = false;
				if (account.getType() != AccountEntity.TYPE_SELL) {
					try {
						loginService.login(account.getUsername(), account.getPassword(), null);
						loginSuccess = true;
					} catch (Exception e) {
						logger.error(e, "登录失败");
					}
				}
				if (loginSuccess) {
					progress.setClose(true);
					return;
				}
				loginDialog.setLoginInfo(account, loginService);
				result.setCode(1);
				result.setSuccess(false);
				progress.setClose(true);
			}
		}.start();
		showLoopProcess(progress);
		if (!result.getSuccess()) {
			if (result.getCode() != null && result.getCode().equals(1)) {
				loginDialog.setVisible(true);
			} else {
				showErrorMessage(result.getMessage());
			}
		}

	}

	private void insertChat(int type, String content, GoodsEntity goods, OrderEntity order) {
		StringBuffer messageContent = new StringBuffer("<div style='color:#C4C4C4'>" + DateUtil.getNowDate() + "</div><br/>");
		messageContent.append("<div style='margin-left:10px;color:#808080'><div>" + content + "</div></div><br/>");
		if (goods != null) {
			messageContent.append("<div style='margin-left:10px;color:#808080'><div>商品名称：" + goods.getName() + "</div><div>价格：" + goods.getCurrentprice() + "</div><div><a href='#'>查看拼多多商品</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href='#'>查看拼淘宝商品</a></div></div><br/>");
		}

		if (order != null) {
			messageContent.append("<div style='margin-left:10px;color:#808080'><div>订单ID：" + order.getSellOrderId() + "</div><div>价格：" + order.getSellAmount() + "</div><div>状态：" + order.getSellOrderState() + "</div></div><br/>");
		}

		try {
			HTMLDocument doc = (HTMLDocument) this.chatTextPane.getDocument();
			switch (type) {
			case 0:
				doc.insertAfterEnd(doc.getDefaultRootElement().getElement(doc.getDefaultRootElement().getElementCount() - 1), "<table style='width:100%;margin-top:20px;'><tr><td style='50%;'>" + messageContent + "</td><td style='width:50%'>&nbsp;</td></tr><table>");
				break;
			case 1:
				doc.insertAfterEnd(doc.getDefaultRootElement().getElement(doc.getDefaultRootElement().getElementCount() - 1), "<table style='width:100%;margin-top:20px;'><tr><td style='50%;'>&nbsp;</td><td style='width:50%'>" + messageContent + "</td></tr><table>");

				break;
			}
		} catch (BadLocationException | IOException e) {
			//
			e.printStackTrace();
		}

	}

	private void chatSendClick() {
		String message = this.chatSendText.getText();
		if (message.trim().length() == 0) {
			return;
		}

		Field<ChatEntity> chatField = chatUserList.getSelectItem();
		if (chatField == null) {
			return;
		}
		JSONObject sendRequestJson = new JSONObject();
		String request_id = System.currentTimeMillis() + "";
		String ts = request_id.substring(0, request_id.length() - 3);
		sendRequestJson.put("cmd", "send_message");
		sendRequestJson.put("request_id", request_id);
		sendRequestJson.put("message", new JSONObject());
		sendRequestJson.getJSONObject("message").put("to", new JSONObject());
		sendRequestJson.getJSONObject("message").getJSONObject("to").put("role", "user");
		sendRequestJson.getJSONObject("message").getJSONObject("to").put("uid", chatField.getValue().getUserId());
		sendRequestJson.getJSONObject("message").put("from", new JSONObject());
		sendRequestJson.getJSONObject("message").getJSONObject("from").put("from", "mall_cs");
		sendRequestJson.getJSONObject("message").put("ts", ts);
		sendRequestJson.getJSONObject("message").put("content", message);
		sendRequestJson.getJSONObject("message").put("msg_id", "99999999999999");
		sendRequestJson.getJSONObject("message").put("type", 0);
		sendRequestJson.getJSONObject("message").put("is_aut", 0);
		sendRequestJson.getJSONObject("message").put("status", "read");
		try {
			// sellServiceMap.get(chatField.getValue().getId()).sendChatMessage(sendRequestJson.toJSONString());
			Map<String, Object> chatMap = new HashMap<String, Object>();
			chatMap.put("type", 1);
			chatMap.put("content", message);
			chatMap.put("goods", null);
			chatMap.put("order", null);
			chatField.getValue().getReadChatList().add(chatMap);
			this.insertChat(1, message, null, null);
			chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void orderGroupClick() {
		OrderEntity order = orderListTable.getSelectData();
		try {
			Runtime.getRuntime().exec("cmd.exe /c start http://mobile.yangkeduo.com/goods.html?goods_id=" + order.getSellGoodsId());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 发货点击
	 */
	private void orderShipClick() {
		Result<Void> result = new Result<Void>();
		ProgressBean progress = new ProgressBean();
		new Thread() {
			@Override
			public void run() {
				ThreadUtil.sleep(1000);

				OrderEntity order = orderListTable.getSelectData();
				if (order == null) {
					result.setMessage("请选择订单");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}

				if (StringUtils.isEmpty(order.getBuyOrderId())) {
					result.setMessage("查找不到对应的买单信息");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}

				if (order.getBuyOrderState().equals(OrderEntity.BUYORDERSTATE_UNSHIP)) {
					result.setMessage("买单未发货");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}

				ISellService sellService = null;

				for (Field<String> field : sellOrderAccountCombo.getComponent().getFields()) {
					sellService = sellServiceMap.get(field.getValue());
					break;
				}
				if (sellService == null) {
					result.setMessage("销售账户【", order.getSellId(), "】未登录");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}

				try {
					sellService.ship(order);
					progress.setClose(true);
				} catch (Exception e) {
					result.setMessage("卖单发货失败");
					result.setSuccess(false);
					progress.setClose(true);
					logger.error(e, result.getMessage());
					return;
				}
			}
		}.start();
		showLoopProcess(progress);
		if (!result.getSuccess()) {
			JOptionPane.showMessageDialog(this, result.getMessage(), "操作失败", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 订单购买
	 * 
	 * @author Administrator 2017年12月31日 Administrator
	 */
	private void orderBuyClick() {
		Result<Void> result = new Result<Void>();
		ProgressBean progress = new ProgressBean();
		new Thread() {
			@Override
			public void run() {
				ThreadUtil.sleep(1000);

				Field<String> selectBuyAccount = buyOrderAccountCombo.getComponent().getSelectItem();
				if (selectBuyAccount == null) {
					result.setMessage("请选择购买账户");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}
				IBuyService buyService = buyServiceMap.get(selectBuyAccount.getValue());

				OrderEntity selectOrder = orderListTable.getSelectData();
				if (selectOrder.getSellOrderState().equals(OrderEntity.SELLORDERSTATE_UNGROUP)) {
					result.setSuccess(false);
					result.setMessage("订单未成团");
					progress.setClose(true);
					return;
				}

				if (StringUtils.isNotEmpty(selectOrder.getBuyOrderId())) {
					result.setSuccess(false);
					result.setMessage("订单已经购买");
					progress.setClose(true);
					return;
				}

				try {
					buyService.buy(selectOrder);
					progress.setClose(true);
				} catch (Exception e) {
					result.setMessage("购买订单失败，错误信息【", e.getMessage(), "】");
					result.setSuccess(false);
					progress.setClose(true);
					logger.error(e, result.getMessage());
					return;
				}
			}
		}.start();
		showLoopProcess(progress);
		if (!result.getSuccess()) {
			JOptionPane.showMessageDialog(this, result.getMessage(), "操作失败", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * 订单查询
	 * 
	 * @author Administrator 2017年12月31日 Administrator
	 */
	private void orderSearchClick() {
		ProgressBean progress = new ProgressBean();
		Result<Void> result = new Result<Void>();
		new Thread() {
			@Override
			public void run() {
				ThreadUtil.sleep(1000);

				Field<String> selectBuyAccount = buyOrderAccountCombo.getComponent().getSelectItem();
				if (selectBuyAccount == null) {
					result.setMessage("请选择购买账户");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}
				IBuyService buyService = buyServiceMap.get(selectBuyAccount.getValue());

				if (buyService == null || !buyService.isLogin()) {
					result.setMessage("购买账户未登录");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}

				Field<String> selectSellAccount = sellOrderAccountCombo.getComponent().getSelectItem();
				List<ISellService> sellServiceList = new ArrayList<ISellService>();
				for (int i = 0; i < sellOrderAccountCombo.getComponent().getFields().length; i++) {
					if (!selectSellAccount.getCode().equals("-1") && !selectSellAccount.getCode().equals(sellOrderAccountCombo.getComponent().getFields()[i].getCode())) {
						continue;
					}
					ILoginService loginService = sellServiceMap.get(sellOrderAccountCombo.getComponent().getFields()[i].getValue());
					if (loginService == null) {
						continue;
					}
					if (!loginService.isLogin()) {
						continue;
					}
					sellServiceList.add((ISellService) loginService);
				}

				if (sellServiceList.isEmpty()) {
					result.setMessage("请选择销售账户，或者所选销售账户未登录");
					result.setSuccess(false);
					progress.setClose(true);
					return;
				}
				orderListTable.clear();
				List<OrderEntity> orderList = new ArrayList<OrderEntity>();
				for (ISellService sellService : sellServiceList) {
					List<OrderEntity> sellOrderList = null;
					try {
						sellOrderList = sellService.getOrderList();
					} catch (Exception e) {
						result.setMessage("查询销售账户【", sellService.getId(), "】订单列表失败，错误信息【", e.getMessage(), "】");
						result.setSuccess(false);
						progress.setClose(true);
						logger.error(e, result.getMessage());
						return;
					}
					if (sellOrderList == null || sellOrderList.isEmpty()) {
						continue;
					}
					try {
						orderList.addAll(buyService.getOrderList(sellOrderList));
					} catch (Exception e) {
						result.setMessage("查询购买账户【", buyService.getId(), "】订单列表失败，错误信息【", e.getMessage(), "】");
						result.setSuccess(false);
						progress.setClose(true);
						logger.error(e, result.getMessage());
						return;
					}
				}
				orderListTable.setDatas(orderList);
				progress.setClose(true);
			}

		}.start();
		showNumberProgress(progress);
		if (!result.getSuccess()) {
			JOptionPane.showMessageDialog(this, result.getMessage(), "操作失败", JOptionPane.ERROR_MESSAGE);
		}
		orderBuyBtn.setEnabled(true);

	}

	@Override
	public void onLogin(Object source) {
		ILoginService loginService = (ILoginService) source;
		String accountId = null;
		for (int i = 0; i < commAccountTable.getDatas().size(); i++) {
			AccountEntity account = commAccountTable.getDatas().get(i);
			accountId = account.getId();
			if (accountId != null && commissionSericeMap.get(accountId) != null && commissionSericeMap.get(accountId).equals(source)) {
				account.setLogin(true);
				account.setStateDate(new Date());
				commAccountTable.setData(i, account);
				boolean hasField = false;
				for (Field<String> field : taskCommAccountCombo.getComponent().getFields()) {
					if (field.getValue().equals(loginService.getId())) {
						hasField = true;
						break;
					}
				}
				if (!hasField) {
					Field<String> field = new Field<>();
					field.setCode(loginService.getId());
					field.setValue(loginService.getId());
					field.setName(loginService.getUsername());
					taskCommAccountCombo.getComponent().addItem(field);
				}
				return;
			}
		}

		for (int i = 0; i < buyAccountTable.getDatas().size(); i++) {
			AccountEntity account = buyAccountTable.getDatas().get(i);
			accountId = account.getId();
			if (accountId != null && buyServiceMap.get(accountId) != null && buyServiceMap.get(accountId).equals(source)) {
				account.setLogin(true);
				account.setStateDate(new Date());
				buyAccountTable.setData(i, account);

				boolean hasField = false;
				for (Field<String> field : buyOrderAccountCombo.getComponent().getFields()) {
					if (field.getValue().equals(loginService.getId())) {
						hasField = true;
						break;
					}
				}
				if (!hasField) {
					Field<String> field = new Field<>();
					field.setCode(loginService.getId());
					field.setValue(loginService.getId());
					field.setName(loginService.getUsername());
					buyOrderAccountCombo.getComponent().addItem(field);
				}
				return;
			}
		}

		for (int i = 0; i < sellAccountTable.getDatas().size(); i++) {
			AccountEntity account = sellAccountTable.getDatas().get(i);
			accountId = account.getId();
			if (accountId != null && sellServiceMap.get(accountId) != null && sellServiceMap.get(accountId).equals(source)) {
				account.setLogin(true);
				account.setStateDate(new Date());
				sellAccountTable.setData(i, account);

				boolean hasField = false;
				for (Field<String> field : sellOrderAccountCombo.getComponent().getFields()) {
					if (field.getValue().equals(loginService.getId())) {
						hasField = true;
						break;
					}
				}
				if (!hasField) {
					Field<String> field = new Field<>();
					field.setCode(loginService.getId());
					field.setValue(loginService.getId());
					field.setName(loginService.getUsername());
					sellOrderAccountCombo.getComponent().addItem(field);
				}

				hasField = false;
				for (Map<String, String> map : this.taskSellAccountTable.getDatas()) {
					if (map.get("id").equals(loginService.getId())) {
						hasField = true;
						break;
					}
				}

				if (!hasField) {
					SellServiceTask task = new SellServiceTask((ISellService) loginService);
					task.addListener(this);
					this.sellServiceTaskMap.put(loginService.getId(), task);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", loginService.getId());
					map.put("username", loginService.getUsername());

					map.put("checkState", task.getCheckState().toString());
					map.put("checkSuccess", task.getCheckSuccess().toString());
					map.put("checkFailure", task.getCheckFailure().toString());
					map.put("publicState", task.getPublicState().toString());
					map.put("publicSuccess", task.getPublicSuccess().toString());
					map.put("publicFailure", task.getPublicFailure().toString());
					map.put("lastTaskTime", task.getLastTaskTime().toString());
					this.taskSellAccountTable.addData(map);
				}

				return;
			}
		}
	}

	@Override
	public void onCodeChange(Object source, File codeFile) {

	}

	@Override
	public void onLogout(Object sourcer) {

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(chatUserList)) {
			chatUserListChange();
		}
	}

	public void chatUserListChange() {
		Field<ChatEntity> selectChatField = this.chatUserList.getSelectItem();
		if (selectChatField == null) {
			return;
		}
		selectChatField.getValue().getReadChatList().addAll(selectChatField.getValue().getUnReadChatList());
		selectChatField.getValue().getUnReadChatList().clear();
		this.chatTextPane.setText("");
		for (Map<String, Object> chatMap : selectChatField.getValue().getReadChatList()) {
			this.insertChat((int) chatMap.get("type"), (String) chatMap.get("content"), (GoodsEntity) chatMap.get("goods"), (OrderEntity) chatMap.get("order"));
		}

		chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());
	}

	@Override
	public void onMessage(Integer type, String sendId, String sendName, String recId, String recName, String content, GoodsEntity goods, OrderEntity order) {
		Map<String, Object> chatMap = new HashMap<String, Object>();
		chatMap.put("type", type);
		chatMap.put("content", content);
		chatMap.put("goods", goods);
		chatMap.put("order", order);

		String chatCode = type == 0 ? recId + "_" + sendId : sendId + "_" + recId;
		Field<ChatEntity> chatField = this.chatUserList.getField(chatCode);
		if (chatField == null) {
			chatField = new Field<ChatEntity>();
			chatField.setCode(chatCode);
			chatField.setName(type == 0 ? sendName : recName);
			ChatEntity chat = new ChatEntity();
			chat.setId(type == 0 ? recId : sendId);
			chat.setUserId(type == 0 ? sendId : recId);
			chat.setNickname(type == 0 ? sendName : recName);
			chat.setReadChatList(new ArrayList<Map<String, Object>>());
			chat.setUnReadChatList(new ArrayList<Map<String, Object>>());
			chatField.setValue(chat);
		}
		Field<ChatEntity> selectChatField = this.chatUserList.getSelectItem();
		if (selectChatField != null && selectChatField.equals(chatField)) {
			chatField.getValue().getReadChatList().add(chatMap);
			this.insertChat(type, content, goods, order);
		} else {
			chatField.getValue().getUnReadChatList().add(chatMap);
			this.chatUserList.removeItem(chatField);
			chatField.setName(type == 0 ? sendName : recName + "(" + this.chatUserList.getValue().getUnReadChatList().size() + ")");
			this.chatUserList.insertItem(chatField, 0);
		}
		chatTextPane.setCaretPosition(chatTextPane.getDocument().getLength());

	}

	@Override
	public void onTaskStateChange(SellServiceTask task, Integer checkState, Integer checkSuccess, Integer checkFailure, Integer publicState, Integer publicSuccess, Integer publicFailure, Long lastTaskTime) {
		String id = null;
		Iterator<String> it = sellServiceTaskMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (sellServiceTaskMap.get(key).equals(task)) {
				id = key;
				break;
			}
		}
		if (StringUtils.isNotEmpty(id)) {
			for (int i = 0; i < taskSellAccountTable.getDatas().size(); i++) {
				Map<String, String> map = taskSellAccountTable.getDatas().get(i);
				if (map.get("id").equals(id)) {
					map.put("checkState", checkState.toString());
					map.put("checkSuccess", checkSuccess.toString());
					map.put("checkFailure", checkFailure.toString());
					map.put("publicState", publicState.toString());
					map.put("publicSuccess", publicSuccess.toString());
					map.put("publicFailure", publicFailure.toString());
					map.put("lastTaskTime", lastTaskTime.toString());
					taskSellAccountTable.setData(i, map);
				}
			}
		}

	}
}
