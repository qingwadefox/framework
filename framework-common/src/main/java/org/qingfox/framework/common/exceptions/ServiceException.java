package org.qingfox.framework.common.exceptions;

import org.apache.commons.lang3.StringUtils;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1598913269411025534L;

	private String code;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Object... message) {
		super(StringUtils.join(message));
	}

	public ServiceException(String code, Object... message) {
		super(StringUtils.join(message));
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
