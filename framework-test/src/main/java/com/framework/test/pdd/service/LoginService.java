/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.framework.test.pdd.service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.framework.common.exceptions.ServiceException;
import com.framework.common.utils.RandomUtil;
import com.framework.test.pdd.service.inf.ILoginService;
import com.framework.test.pdd.service.listeners.LoginListener;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2018年1月18日 @功能说明：
 * 
 */
public abstract class LoginService implements ILoginService {
	private String id;
	private Boolean login;
	private List<LoginListener> loginListeners;
	private String username;
	private String password;

	public LoginService() {
		login = false;
		id = RandomUtil.getSimpleUUID();
		loginListeners = new LinkedList<LoginListener>();
	}

	public String getId() {
		return id;
	}

	protected void setId(String id) {
		this.id = id;
	}

	public boolean isLogin() {
		return login;
	}

	protected void setLogin(Boolean login) {
		this.login = login;
	}

	protected void checkLogin() throws ServiceException {
		if (!isLogin()) {
			throw new ServiceException("服务未登录");
		}
	}

	public String getUsername() {
		return username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}

	protected String getPassword() {
		return password;
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected void onLogin() {
		for (LoginListener listener : loginListeners) {
			listener.onLogin(this);
		}
	}

	protected void onLogout() {
		for (LoginListener listener : loginListeners) {
			listener.onLogout(this);
		}
	}

	protected void onCodeChange(File codeFile) {
		for (LoginListener listener : loginListeners) {
			listener.onCodeChange(this, codeFile);
		}
	}

	@Override
	public void addLoginListener(LoginListener listener) {
		this.loginListeners.add(listener);
	}

	@Override
	public void removeLoginListener(LoginListener listener) {
		this.loginListeners.remove(listener);
	}
}
