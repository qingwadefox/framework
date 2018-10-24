package org.qingfox.framework.common.tool.websocket;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.qingfox.framework.common.tool.websocket.listener.WebsSocketListener;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class WebSocketClient {
	private org.eclipse.jetty.websocket.client.WebSocketClient client;
	private SimpleEchoSocket echoSocket;

	public WebSocketClient() {
		this.client = new org.eclipse.jetty.websocket.client.WebSocketClient();
		this.echoSocket = new SimpleEchoSocket();
	}

	public void connect(String url, int duration, TimeUnit unit) throws Exception {
		ClientUpgradeRequest request = new ClientUpgradeRequest();
		URI echoUri = new URI(url);
		this.client.start();
		this.client.connect(echoSocket, echoUri, request);
	}

	public void addListener(WebsSocketListener listener) {
		this.echoSocket.addListener(listener);
	}

	public void sendMessage(String message) throws IOException {
		this.echoSocket.sendMessage(message);
	}
}
