package com.framework.common.log;

public interface ILogger {

	public void debug(Object... message);

	public void debug(Throwable t, Object... message);

	public void info(Object... message);

	public void info(Throwable t, Object... message);

	public void warn(Object... message);

	public void warn(Throwable t, Object... message);

	public void error(Object... message);

	public void error(Throwable t, Object... message);

}
