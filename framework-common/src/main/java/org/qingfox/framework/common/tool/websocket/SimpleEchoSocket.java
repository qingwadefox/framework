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
package org.qingfox.framework.common.tool.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;
import org.qingfox.framework.common.tool.websocket.listener.WebsSocketListener;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2018年1月8日
 * @功能说明：
 * 
 */
@WebSocket(maxTextMessageSize = 64 * 1024)
public class SimpleEchoSocket {
	private static final ILogger logger = LoggerFactory.getLogger(SimpleEchoSocket.class);

	private final CountDownLatch closeLatch;
	private Session session;
	private List<WebsSocketListener> listeners;

	public SimpleEchoSocket() {
		this.closeLatch = new CountDownLatch(1);
		this.listeners = new ArrayList<WebsSocketListener>();
	}

	public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
		return this.closeLatch.await(duration, unit);
	}

	public void sendMessage(String message) throws IOException {
		logger.debug("session send:", message);
		this.session.getRemote().sendString(message);
	}

	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		logger.debug("Connection closed:", statusCode, " - ", reason);
		this.session = null;
		this.closeLatch.countDown();
		for (WebsSocketListener listener : listeners) {
			listener.onClose(statusCode, reason);
		}
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		logger.debug("Got connect:", session);
		this.session = session;
		for (WebsSocketListener listener : listeners) {
			listener.onConnect(session);
		}
	}

	@OnWebSocketMessage
	public void onMessage(String msg) {
		logger.debug("Got msg:", msg);
		for (WebsSocketListener listener : listeners) {
			listener.onMessage(msg);
		}
	}
	public void addListener(WebsSocketListener listener) {
		listeners.add(listener);
	}
}
