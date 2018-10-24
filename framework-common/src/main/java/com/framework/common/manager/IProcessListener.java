package com.framework.common.manager;

public interface IProcessListener {
	public void onStart(String key);

	public void onRunning(String key);

	public void onSuccess(String key);

	public void onFailed(String key);

}
