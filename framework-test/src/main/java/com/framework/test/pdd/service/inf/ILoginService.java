/**
 * 
 */
package com.framework.test.pdd.service.inf;

import com.framework.test.pdd.service.listeners.LoginListener;

/**
 * @author zhengwei
 *
 */
public interface ILoginService {
	public void login(String username, String password, String code) throws Exception;

	public void refreshCode() throws Exception;

	public void addLoginListener(LoginListener listener);

	public void removeLoginListener(LoginListener listener);

	public boolean isLogin();

	public String getId();

	public String getUsername();

}
