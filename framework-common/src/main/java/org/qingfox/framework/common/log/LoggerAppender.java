package org.qingfox.framework.common.log;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class LoggerAppender implements ILogger {

	private Logger logger;

	public LoggerAppender(Class<?> clazz) {
		logger = org.slf4j.LoggerFactory.getLogger(clazz);
	}

	public void debug(Object... message) {
		if (logger.isDebugEnabled()) {
			logger.debug(StringUtils.join(message));
		}
	}

	public void debug(Throwable t, Object... message) {
		if (logger.isDebugEnabled()) {
			logger.debug(StringUtils.join(message), t);
		}
	}

	public void info(Object... message) {
		if (logger.isInfoEnabled()) {
			logger.info(StringUtils.join(message));
		}
	}

	public void info(Throwable t, Object... message) {
		if (logger.isInfoEnabled()) {
			logger.info(StringUtils.join(message), t);
		}
	}

	public void warn(Object... message) {
		if (logger.isWarnEnabled()) {
			logger.warn(StringUtils.join(message));
		}
	}

	public void warn(Throwable t, Object... message) {
		if (logger.isWarnEnabled()) {
			logger.warn(StringUtils.join(message), t);
		}
	}

	public void error(Object... message) {
		if (logger.isErrorEnabled()) {
			logger.error(StringUtils.join(message));
		}
	}

	public void error(Throwable t, Object... message) {
		if (logger.isErrorEnabled()) {
			logger.error(StringUtils.join(message), t);
		}
	}

}
