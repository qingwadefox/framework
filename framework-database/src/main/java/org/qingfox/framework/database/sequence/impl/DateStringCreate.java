package org.qingfox.framework.database.sequence.impl;


import org.qingfox.framework.database.sequence.ISequenceCreate;

import org.qingfox.framework.common.utils.DateUtil;

public class DateStringCreate implements ISequenceCreate<String> {

	private String format = DateUtil.PATTERN_YYYYMMDDHHMMSS;
	private String current;
	private String previous;

	public String next() {
		String next = DateUtil.getNowDate(format);
		previous = current;
		current = next;
		return next;
	}

	public String current() {
		return current;
	}

	public String previous() {
		return previous;
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
