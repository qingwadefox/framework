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
package com.framework.common.thread;

import com.framework.common.exceptions.ThreadExitException;
import com.framework.common.utils.ThreadUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see: @创建日期：2018年1月19日 @功能说明：
 * 
 */
public abstract class TaskThread extends Thread {
	private boolean exit = false;
	private boolean pause = false;
	private Long pauseCheckTime = 1000L;

	@Override
	public void run() {
		try {
			runTask();
		} catch (ThreadExitException e) {
			return;
		} catch (Exception e) {
			this.exit();
			e.printStackTrace();
		}
	}

	protected abstract void runTask() throws Exception;

	public void check() throws ThreadExitException {
		checkExit();
		checkPause();
	}

	public void checkExit() throws ThreadExitException {
		if (this.exit) {
			throw new ThreadExitException();
		}
	}

	public void checkPause() {
		while (!this.pause) {
			ThreadUtil.sleep(pauseCheckTime);
		}
	}

	public void exit() {
		this.exit = true;
	}
	public void pause() {
		pause = true;
	}

	public void proceed() {
		pause = false;
	}

	public boolean isPause() {
		return pause;
	}

}
