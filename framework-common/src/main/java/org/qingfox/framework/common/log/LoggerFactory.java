package org.qingfox.framework.common.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.qingfox.framework.common.utils.ProcessUtil;
import org.slf4j.MDC;

/**
 * 日志工厂类.
 * 
 *
 * @author liujd
 * @version Rversion 1.0.0 *修改时间 | 修改内容
 *
 */
public class LoggerFactory {
	private static final ConcurrentMap<Class<?>, ILogger> loggerResMap = new ConcurrentHashMap<Class<?>, ILogger>();
	private static final Object lock = new Object();

	private static final String MDC_NETID = "netId";
	private static final String MDC_PROCNAME = "procName";
	private static final String MDC_PID = "PID";

	static {
		if (!hasMDC(MDC_NETID)) {
			try {
				String netId = InetAddress.getLocalHost().getHostName();
				setNetId(netId);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		if (!hasMDC(MDC_PID)) {
			setProcId(ProcessUtil.getPID());
		}

		if (!hasMDC(MDC_PROCNAME)) {
			setProcName("defaultProc");
		}

	}

	private static boolean hasMDC(String key) {
		if (StringUtils.isEmpty(MDC.get(key))) {
			return false;
		} else {
			return true;
		}
	}

	public static void putMDC(String key, String value) {
		MDC.put(key, value);
	}

	public static void setNetId(String netId) {
		MDC.put(MDC_NETID, netId);
	}

	public static void setProcId(String id) {
		MDC.put(MDC_PID, id);
	}

	public static void setProcName(String name) {
		MDC.put(MDC_PROCNAME, name);
	}

	public static ILogger getLogger(Class<?> name) {
		ILogger logger = loggerResMap.get(name);
		if (logger == null) {
			synchronized (lock) {
				logger = loggerResMap.get(name);
				if (logger == null) {
					logger = new LoggerAppender(name);
					loggerResMap.put(name, logger);
				}
			}
		}
		return logger;
	}

}
