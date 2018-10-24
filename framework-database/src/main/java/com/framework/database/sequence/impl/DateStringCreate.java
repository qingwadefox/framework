package com.framework.database.sequence.impl;


import com.framework.common.utils.DateUtil;
import com.framework.database.sequence.ISequenceCreate;

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
