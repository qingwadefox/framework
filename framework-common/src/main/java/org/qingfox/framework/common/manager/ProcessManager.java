package org.qingfox.framework.common.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class ProcessManager {

	public final static int PROCESS_STATUS_START = 0;
	public final static int PROCESS_STATUS_RUNNING = 1;
	public final static int PROCESS_STATUS_SUCCESS = 2;
	public final static int PROCESS_STATUS_FAILED = 3;
	private final static long timeout = 10000L;

	private static boolean init = false;
	public static Map<String, Integer> processMap;
	public static Map<String, List<IProcessListener>> listenerMap;
	static {
		if (!init) {
			processMap = new HashMap<String, Integer>();
			listenerMap = new HashMap<String, List<IProcessListener>>();
		}
	}

	public static void start(String key) {
		processMap.put(key, PROCESS_STATUS_START);
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList != null && !listenerList.isEmpty()) {
			for (IProcessListener listener : listenerList) {
				listener.onStart(key);
			}
		}
	}

	public static void running(String key) {
		processMap.put(key, PROCESS_STATUS_RUNNING);
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList != null && !listenerList.isEmpty()) {
			for (IProcessListener listener : listenerList) {
				listener.onRunning(key);
			}
		}
	}

	public static void failed(String key) {
		processMap.put(key, PROCESS_STATUS_FAILED);
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList != null && !listenerList.isEmpty()) {
			for (IProcessListener listener : listenerList) {
				listener.onFailed(key);
			}
		}
	}

	public static void success(String key) {
		processMap.put(key, PROCESS_STATUS_SUCCESS);
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList != null && !listenerList.isEmpty()) {
			for (IProcessListener listener : listenerList) {
				listener.onSuccess(key);
			}
		}
	}

	public static Boolean waitProcessStatus(String key, Integer... status) {
		return waitProcessStatus(timeout, key, status);
	}

	public static Boolean waitProcessStatus(Long timeout, String key, Integer... status) {

		if (timeout == null) {
			return false;
		}

		if (StringUtils.isEmpty(key)) {
			return false;
		}
		for (Integer _status : status) {
			if (_status == null || _status < 0 || _status > 3) {
				return false;
			}
		}

		Integer nowStatus = null;
		Long timeoutcount = timeout / 1000;
		Long timeoutnowcount = 0L;

		while ((nowStatus = processMap.get(key)) == null || ArrayUtils.indexOf(status, nowStatus) == -1 || timeoutnowcount < timeoutcount) {
			timeoutnowcount++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (nowStatus == null || ArrayUtils.indexOf(status, nowStatus) == -1) {
			return false;
		} else {
			return true;
		}
	}

	public static int getProcessStatus(String key) {
		return processMap.get(key);
	}

	public static void addListener(String key, IProcessListener listener) {
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList == null) {
			listenerList = new ArrayList<IProcessListener>();
		}
		if (listenerList.indexOf(listener) == -1) {
			listenerList.add(listener);
		}
		listenerMap.put(key, listenerList);
	}

	public static void removeListener(String key, IProcessListener listener) {
		List<IProcessListener> listenerList = listenerMap.get(key);
		if (listenerList == null) {
			listenerList = new ArrayList<IProcessListener>();
		}
		if (listenerList.indexOf(listener) != -1) {
			listenerList.remove(listener);
		}
	}
}
