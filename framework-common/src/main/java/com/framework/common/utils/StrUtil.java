package com.framework.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StrUtil {

	// /**
	// * 获取拼音首字母
	// *
	// * @param strs
	// * 拼音参数
	// * @return
	// */
	// public static String getHeaders(String strs) {
	// StringBuffer headers = new StringBuffer();
	// char[] chars = strs.toCharArray();
	//
	// for (char _char : chars) {
	// try {
	//
	// String _str = PinyinHelper.toHanyuPinyinStringArray(_char)[0];
	// headers.append(_str.toUpperCase().toCharArray()[0]);
	// } catch (Exception e) {
	// continue;
	// }
	// }
	//
	// return headers.toString();
	// }

	/**
	 * 组合字符串
	 * 
	 * @param strings
	 *            字符串
	 * @return 组合后的字符串
	 */
	public static String join(Object... strs) {
		return StringUtils.join(strs);
	}

	/**
	 * 根据分隔符组合字符串
	 * 
	 * @param separator
	 *            分隔符
	 * @param strings
	 *            字符串
	 * @return 组合后的字符串
	 */
	public static String joinSep(String separator, Object... strs) {
		return StringUtils.join(strs, separator);
	}

	/**
	 * 参数转换为字符串
	 * 
	 * @param object
	 *            被转换的参数
	 * @return 字符串
	 */
	public static String toString(Object object) {
		return ConvertUtil.convert(object, java.lang.String.class);
	}

	/**
	 * 如果为空或者为NULL 转换为指定值
	 * 
	 * @param srcStr
	 *            被判断的字符串
	 * @param objStr
	 *            转换后的字符串
	 * @return 为空返回[objStr]，否则返回[srcStr]
	 */
	public static String emptyToValue(String srcStr, String objStr) {
		if (StringUtils.isEmpty(srcStr)) {
			return objStr;
		} else {
			return srcStr;
		}
	}

	/**
	 * 转换为NULL
	 * 
	 * @param srcStr
	 *            被判断的字符串
	 * @return 为空返回[NULL]，否则返回[srcStr]
	 */
	public static String emptyToNull(String srcStr) {
		if (StringUtils.isEmpty(srcStr)) {
			return null;
		} else {
			return srcStr;
		}
	}

	/**
	 * NULL转换为空字符
	 * 
	 * @param srcStr
	 *            被判断的字符串
	 * @return 为空返回[""]，否则返回[srcStr]
	 */
	public static String nullToEmpty(String srcStr) {
		if (srcStr == null) {
			return "";
		} else {
			return srcStr;
		}
	}

	/**
	 * 根据开始以及结束位子将相关的小写转为大些
	 * 
	 * @param str
	 *            判断的字符串
	 * @param strat
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return 字符串
	 */
	public static String toUpperCase(String str, int strat, int end) {
		return str.substring(strat, end).toUpperCase() + str.substring(end, str.length());
	}

	/**
	 * 将第一个字符转化为大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstUpperCase(String str) {
		return toUpperCase(str, 0, 1);
	}

	/**
	 * 按照位数截取 .
	 * 
	 * @param str
	 * @param point
	 * @return
	 * @author Administrator 2017年12月21日 Administrator
	 */
	public static String[] splitByPoint(String str, Integer point) {
		String[] strs = null;
		String lesStr = str;
		if (str.length() < point) {
			return ArrayUtils.add(strs, str);
		}
		do {
			strs = ArrayUtils.add(strs, lesStr.substring(0, point));
			lesStr = lesStr.substring(point, lesStr.length());
		} while (lesStr.length() >= point);

		if (lesStr.length() > 0) {
			strs = ArrayUtils.add(strs, lesStr);
		}

		return strs;
	}

	public static void readLine(String content, ReadLine readLine) {
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
		String line;
		int number = 0;
		try {
			while ((line = br.readLine()) != null) {
				readLine.nextLine(line, number);
				number++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String shorten(String text, int maxLen) {
		if (text.length() > maxLen) {
			return text.substring(0, maxLen);
		} else {
			return text;
		}
	}

	public static String repParam(String text, Object... params) {
		if (params == null || params.length == 0) {
			return text;
		}

		for (int i = 0; i < params.length; i++) {
			text = text.replace("{" + i + "}", params[i].toString());
		}
		return text;
	}

}
