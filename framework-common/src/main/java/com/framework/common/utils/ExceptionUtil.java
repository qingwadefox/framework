package com.framework.common.utils;

public class ExceptionUtil {

	public String getDetail(Exception e) {
		StringBuffer msg = new StringBuffer("");

		if (e != null) {
			msg = new StringBuffer("");

			String message = e.toString();

			int length = e.getStackTrace().length;

			if (length > 0) {

				msg.append(message + "\n");

				for (int i = 0; i < length; i++) {

					msg.append("\t" + e.getStackTrace()[i] + "\n");

				}
			} else {

				msg.append(message);
			}

		}
		return msg.toString();
	}
}
