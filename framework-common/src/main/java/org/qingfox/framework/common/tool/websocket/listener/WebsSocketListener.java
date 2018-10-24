package org.qingfox.framework.common.tool.websocket.listener;

import org.eclipse.jetty.websocket.api.Session;

public interface WebsSocketListener {
	public void onClose(int statusCode, String reason);

	public void onConnect(Session session);

	public void onMessage(String msg);
}
