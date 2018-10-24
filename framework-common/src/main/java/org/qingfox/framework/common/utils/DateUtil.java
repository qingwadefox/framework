package org.qingfox.framework.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.qingfox.framework.common.enums.DateEnum;

public class DateUtil {

	public static String ORACLE_PATTERN = "YYYY-MM-DD HH24:MI:SS";
	public static String MYSQL__PATTERN = "%Y-%m-%d %H:%i:%s";
	public static String PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static String PATTERN_YYYYMMDD = "yyyyMMdd";
	public static String HMS_MIN = "00:00:00";
	public static String HMS_MAX = "23:59:59";

	public final static int SECOND = 1;
	public final static int MINUTE = 2;
	public final static int HOUR = 3;
	public final static int DAY = 4;
	public final static int WEEK = 5;;
	public final static int MONTH = 6;
	public final static int YEAR = 7;

	public static Date parse(String date) throws ParseException {
		return parse(date, null);
	}

	/**
	 * 根据格式将字符串转换为日期
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parse(String date, String pattern) throws ParseException {
		if (StringUtils.isEmpty(pattern)) {
			pattern = getPattern(date);
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}

	/**
	 * 格式化DATE类型
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 格式化Calendar类型
	 * 
	 * @param calendar
	 * @param pattern
	 * @return
	 */
	public static String format(Calendar calendar, String pattern) {
		return format(calendar.getTime(), pattern);
	}

	/**
	 * 格式化Calendar类型
	 * 
	 * @param calendar
	 * @return
	 */
	public static String format(Calendar calendar) {
		return format(calendar, PATTERN);
	}

	public static String format(Long millis, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return format(calendar, format);
	}

	/**
	 * 格式化date类型
	 * 
	 * @param date
	 * @return
	 */
	public static String format(Date date) {
		return format(date, PATTERN);
	}

	public static String format(Long millis) {
		return format(millis, PATTERN);
	}

	/**
	 * 根据日期字符串获取数据库日期格式(标准格式判断：年份必须为4位，其他必须为两位)
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getOraclePattern(String dateStr) {
		int lenth = dateStr.length();
		return StringUtils.substring(ORACLE_PATTERN.replace("HH24", "HH"), 0, lenth).replace("HH", "HH24");
	}

	public static String getMySqlPattern(String dateStr) {
		int lenth = dateStr.length();
		return StringUtils.substring(MYSQL__PATTERN, 0, lenth);
	}

	/**
	 * 根据日期字符串获取日期格式(标准格式判断：年份必须为4位，其他必须为两位)
	 * 
	 * @param dateStr
	 * @return
	 */
	public static String getPattern(String dateStr) {
		int lenth = dateStr.length();
		return StringUtils.substring(PATTERN, 0, lenth);
	}

	/**
	 * 根据格式获取当前时间
	 * 
	 * @return
	 */
	public static String getNowDate() {
		return getNowDate(PATTERN);
	}

	/**
	 * 根据格式获取当前时间
	 * 
	 * @return
	 */
	public static String getNowDate(String format) {
		Calendar c = Calendar.getInstance();
		return format(c.getTime(), format);
	}

	/**
	 * 根据格式获取当前日期 .
	 * 
	 * @param format
	 * @return
	 * @author Administrator 2018年1月19日 Administrator
	 */
	public static String current(String format) {
		Calendar c = Calendar.getInstance();
		return format(c.getTime(), format);
	}

	/**
	 * 根据格式获取当前日期 yyyy-MM-dd HH:mm:ss .
	 * 
	 * @return
	 * @author Administrator 2018年1月19日 Administrator
	 */
	public static String currentFormat() {
		return current(PATTERN);
	}

	/**
	 * 获取无格式字符串 yyyyMMdd.
	 * 
	 * @return
	 * @author Administrator 2018年1月19日 Administrator
	 */
	public static String currentDate() {
		Calendar c = Calendar.getInstance();
		return format(c.getTime(), PATTERN_YYYYMMDD);
	}

	/**
	 * 获取当月共有几天 42天格式
	 * 
	 * @param calendar
	 * @return
	 */
	public static List<Calendar> getDateList42(Calendar calendar) {
		List<Calendar> list = new ArrayList<Calendar>();
		Calendar _calendar = Calendar.getInstance();
		_calendar.setTimeInMillis(calendar.getTimeInMillis());
		_calendar.set(Calendar.DAY_OF_MONTH, 1);
		int index = _calendar.get(Calendar.DAY_OF_WEEK);
		int sum = (index == 1 ? 8 : index);
		_calendar.add(Calendar.DAY_OF_MONTH, 0 - sum);
		for (int i = 0; i < 42; i++) {
			_calendar.add(Calendar.DAY_OF_MONTH, 1);
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(_calendar.getTimeInMillis());
			list.add(ca);
		}
		return list;
	}

	/**
	 * 获取当月共有几天 42天格式
	 * 
	 * @param date
	 * @return
	 */
	public static List<Calendar> getDateList42(Date date) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(date.getTime());
		return getDateList42(ca);
	}

	/**
	 * copy
	 * 
	 * @param ca
	 * @return
	 */
	public static Calendar copy(Calendar ca) {
		Calendar _ca = Calendar.getInstance();
		_ca.setTimeInMillis(ca.getTimeInMillis());
		return _ca;

	}

	public static Integer comparison(Calendar cl1, Calendar cl2, DateEnum dateens) {
		int dstance = 0;
		switch (dateens) {
			case SECOND :
				dstance = (int) ((cl1.getTimeInMillis() - cl2.getTimeInMillis()) / (1000));
				break;
			case HOUR :
				dstance = (int) ((cl1.getTimeInMillis() - cl2.getTimeInMillis()) / (3600 * 1000));
				break;
			case MINUTE :
				dstance = (int) ((cl1.getTimeInMillis() - cl2.getTimeInMillis()) / (60 * 1000));
				break;
			case DAY :
				dstance = (int) ((cl1.getTimeInMillis() - cl2.getTimeInMillis()) / (24 * 3600 * 1000));
				break;
			case WEEK :
				// TODO 补充
				break;
			case MONTH :
				// TODO 补充
				break;
			default :
				break;
		}

		return dstance;
	}
}
