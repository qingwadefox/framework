package org.qingfox.framework.swing.form.panels.fields.impl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import org.qingfox.framework.common.utils.DateUtil;
import org.qingfox.framework.swing.borders.FilletLineBorder;
import org.qingfox.framework.swing.components.labels.EllipseLabel;
import org.qingfox.framework.swing.components.panels.LayoutPanel;
import org.qingfox.framework.swing.form.panels.fields.IField;

public class DateField extends LayoutPanel implements IField<Date>,
		MouseListener {

	private static final long serialVersionUID = -7215133968469745756L;

//	private int module = MODULE_DATE;

	private int nowModule = MODULE_DATE;

	public final static int MODULE_DATETIME = 0;
	public final static int MODULE_DATE = 1;
	public final static int MODULE_MONTH = 2;
	public final static int MODULE_YEAR = 3;

	private final static String[] WEEK_NAMES = { "星期日", "星期一", "星期二", "星期三",
			"星期四", "星期五", "星期六" };

	private final static String[] MONTH_NAMES = { "一月", "二月", "三月", "四月", "五月",
			"六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };

	private List<CalendarEllipseLabel> dateLabelList;

	private List<CalendarEllipseLabel> monthLabelList;

	private JButton preButton;

	private JButton nextButton;

	private LayoutPanel centerPanel;

	private LayoutPanel datePanel;

	private LayoutPanel monthPanel;

	private JLabel bottomLabel;

	private JLabel topLabel;

	private Font contentFont;

	private Color selectColor;

	private Color unselectColor;

	private FilletLineBorder selectBorder;

	private FilletLineBorder unselectBorder;

	private CalendarEllipseLabel selectLabel;

	private Calendar selectCalendar;

	private class CalendarEllipseLabel extends EllipseLabel {

		private static final long serialVersionUID = -5187560943012310305L;

		private Calendar calendar;

		public CalendarEllipseLabel(String arg0, int arg1) {
			super(arg0, arg1);

		}

		public Calendar getCalendar() {
			return calendar;
		}

		public void setCalendar(Calendar calendar) {
			this.calendar = calendar;
		}

	}

	public DateField() {
		super();
		init();
	}

	private void init() {
		initParam();
		initTop();
		initCenter();
		initBottom();
		setValue(new Date());
	}

	private void initParam() {
		contentFont = new Font("微软雅黑", Font.BOLD, 15);
		selectColor = new Color(0, 204, 255, 255);
		selectBorder = new FilletLineBorder(selectColor, 2, true);
		unselectColor = Color.LIGHT_GRAY;
		unselectBorder = new FilletLineBorder(unselectColor, 2, true);
		this.setInsets(1, 1, 1, 1);
		this.setBackground(Color.WHITE);
	}

	private void initTop() {
		preButton = new JButton("<");
		preButton.setFont(contentFont);
		preButton.setMinimumSize(new Dimension(1, 1));
		preButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		preButton.addMouseListener(this);
		this.addComponent(preButton, 0, 0, 1, 1);
		topLabel = new JLabel("", JLabel.CENTER);
		topLabel.setFont(contentFont);
		topLabel.setMinimumSize(new Dimension(1, 1));
		topLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		topLabel.setBorder(unselectBorder);
		topLabel.addMouseListener(this);
		this.addComponent(topLabel, 1, 0, 19, 1);
		nextButton = new JButton(">");
		nextButton.setFont(contentFont);
		nextButton.setMinimumSize(new Dimension(1, 1));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextButton.addMouseListener(this);
		this.addComponent(nextButton, 20, 0, 1, 1);
	}

	private void initCenter() {
		centerPanel = new LayoutPanel();
		this.addComponent(centerPanel, 0, 1, 21, 30);
		initMonthPanel();
		initDatePanel();
	}

	private void initBottom() {
		bottomLabel = new JLabel("test", JLabel.CENTER);
		this.addComponent(bottomLabel, 0, 31, 21, 1);
	}

	private void initDatePanel() {
		UIManager.put("Label.background", new ColorUIResource(
				BeautyEyeLNFHelper.commonBackgroundColor));
		datePanel = new LayoutPanel();
		datePanel.setInsets(2, 2, 2, 2);
		for (String weekName : WEEK_NAMES) {
			JLabel label = new JLabel(weekName, JLabel.CENTER);
			label.setFont(contentFont);
			datePanel.addComponent(label);
		}

		dateLabelList = new ArrayList<CalendarEllipseLabel>();
		for (int y = 1; y < 7; y++) {
			for (int x = 0; x < WEEK_NAMES.length; x++) {
				CalendarEllipseLabel ceLabel = new CalendarEllipseLabel("",
						JLabel.CENTER);
				this.setUnselectLable(ceLabel);
				ceLabel.setCursor(Cursor
						.getPredefinedCursor(Cursor.HAND_CURSOR));
				ceLabel.setFont(contentFont);
				ceLabel.addMouseListener(this);
				dateLabelList.add(ceLabel);
				datePanel.addComponent(ceLabel, x, y, 1, 1);
			}
		}
		centerPanel.addComponent(datePanel);

	}

	private void initMonthPanel() {
		UIManager.put("Label.background", new ColorUIResource(
				BeautyEyeLNFHelper.commonBackgroundColor));
		monthPanel = new LayoutPanel();
		monthPanel.setInsets(2, 2, 2, 2);
		monthLabelList = new ArrayList<CalendarEllipseLabel>();

		int x = 0;
		int y = 0;
		for (String monthName : MONTH_NAMES) {

			if (x == 4) {
				x = 0;
				y++;
			}

			CalendarEllipseLabel ceLabel = new CalendarEllipseLabel(monthName,
					JLabel.CENTER);
			this.setUnselectLable(ceLabel);
			ceLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			ceLabel.setFont(contentFont);
			ceLabel.addMouseListener(this);
			monthLabelList.add(ceLabel);
			monthPanel.addComponent(ceLabel, x, y, 1, 1);
			x++;
		}
		centerPanel.addComponent(monthPanel);
		monthPanel.setVisible(false);
	}

	public void refModule() {

		if (nowModule == MODULE_DATE) {
			topLabel.setText(DateUtil.format(this.selectCalendar, "YYYY-MM"));
			monthPanel.setVisible(false);
			datePanel.setVisible(true);
			List<Calendar> list = DateUtil.getDateList42(selectCalendar);
			int x = 0, y = 0;
			for (Calendar cl : list) {
				if (x == 6) {
					x = 0;
					y++;
				}
				CalendarEllipseLabel ceLabel = dateLabelList.get(x + y * 6);
				if (ceLabel != null) {
					ceLabel.setText(cl.get(Calendar.DATE) + "");
					ceLabel.setCalendar(cl);
					this.setUnselectLable(ceLabel);
					if (cl.get(Calendar.DATE) == this.selectCalendar
							.get(Calendar.DATE)
							&& cl.get(Calendar.MONTH) == this.selectCalendar
									.get(Calendar.MONTH)) {
						this.setSelectLabel(ceLabel);
					}

					if (cl.get(Calendar.MONTH) == selectCalendar
							.get(Calendar.MONTH)) {
						ceLabel.setForeground(Color.BLACK);
					} else {
						ceLabel.setForeground(Color.LIGHT_GRAY);
					}
				}
				x++;
			}
		} else if (nowModule == MODULE_MONTH) {
			topLabel.setText(DateUtil.format(this.selectCalendar, "YYYY"));
			monthPanel.setVisible(true);
			datePanel.setVisible(false);
			int month = this.selectCalendar.get(Calendar.MONTH);
			for (int i = 0; i < monthLabelList.size(); i++) {
				CalendarEllipseLabel ceLabel = monthLabelList.get(i);
				Calendar cl = DateUtil.copy(selectCalendar);
				cl.set(Calendar.MONTH, i);
				ceLabel.setCalendar(cl);
				this.setUnselectLable(ceLabel);
				if (month == i) {
					this.setSelectLabel(ceLabel);
				}
			}

		}
	}

	public Date getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setValue(Date value) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(value);
		selectCalendar = cl;
		refModule();
	}

	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {

		// EllipseLabel ellipseLabel = (EllipseLabel) e.getSource();
		// for (EllipseLabel _ellipseLabel : dateLabelList) {
		// if (selectEllipseLabel == null
		// || !selectEllipseLabel.equals(_ellipseLabel)) {
		// _ellipseLabel.setBorder(new FilletLineBorder(Color.LIGHT_GRAY,
		// 2, true));
		// }
		// }
		// ellipseLabel.setBorder(new FilletLineBorder(selectColor, 2, true));

	}

	public void mouseClicked(MouseEvent e) {

		if (e.getSource().equals(topLabel)) {
			if (nowModule == MODULE_DATE) {
				nowModule = MODULE_MONTH;
			}
			refModule();
		} else if (e.getSource().getClass().equals(CalendarEllipseLabel.class)) {
			CalendarEllipseLabel selectLabel = (CalendarEllipseLabel) e
					.getSource();
			if (!selectLabel.equals(this.selectLabel)) {
				this.setSelectLabel(selectLabel);
				if (this.selectLabel != null) {
					setUnselectLable(this.selectLabel);
				}
				this.selectLabel = selectLabel;
				this.selectCalendar = this.selectLabel.getCalendar();
			}

			if (nowModule == MODULE_MONTH) {
				nowModule = MODULE_DATE;
			}
			refModule();
		} else if (e.getSource().equals(nextButton)) {
			next();
		} else if (e.getSource().equals(preButton)) {
			pre();
		}

	}

	private void next() {
		if (nowModule == MODULE_DATE) {
			this.selectCalendar.add(Calendar.MONTH, 1);
		} else if (nowModule == MODULE_MONTH) {
			this.selectCalendar.add(Calendar.YEAR, 1);
		}
		refModule();
	}

	private void pre() {
		if (nowModule == MODULE_DATE) {
			this.selectCalendar.add(Calendar.MONTH, -1);
		} else if (nowModule == MODULE_MONTH) {
			this.selectCalendar.add(Calendar.YEAR, -1);
		}
		refModule();

	}

	private void setSelectLabel(CalendarEllipseLabel selectLabel) {
		selectLabel.setOpaque(true);
		selectLabel.setBorder(selectBorder);
		selectLabel.setBackground(selectColor);
		selectLabel.setForeground(Color.WHITE);
	}

	private void setUnselectLable(CalendarEllipseLabel selectLabel) {
		selectLabel.setOpaque(false);
		selectLabel.setBackground(null);
		selectLabel.setForeground(Color.BLACK);
		selectLabel.setBorder(unselectBorder);
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseEntered(MouseEvent e) {
		if (e.getSource().equals(topLabel)) {
			topLabel.setBorder(selectBorder);
		} else if (e.getSource().getClass().equals(CalendarEllipseLabel.class)) {
			CalendarEllipseLabel label = (CalendarEllipseLabel) e.getSource();
			label.setBorder(selectBorder);
		}

	}

	public void mouseExited(MouseEvent e) {
		if (e.getSource().equals(topLabel)) {
			topLabel.setBorder(unselectBorder);
		} else if (e.getSource().getClass().equals(CalendarEllipseLabel.class)) {
			CalendarEllipseLabel label = (CalendarEllipseLabel) e.getSource();
			if (!label.equals(this.selectLabel)) {
				label.setBorder(unselectBorder);
			}
		}
	}

}
