package com.framework.plugin.web.common.server.caches;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.stereotype.Cachecode;

@Cachecode(code = "WebSocketSessionCache")
public class WebSocketSessionCache implements CacheInf<WebSocketSession> {

	private Map<String, WebSocketSession> webSocketSessionMap;

	public void create() {
		webSocketSessionMap = new HashMap<String, WebSocketSession>();
	}

	public void destroy() {

	}

	public void refresh() {

	}

	public WebSocketSession get(String key) {
		return webSocketSessionMap.get(key);
	}

	@Override
	public void set(String key, WebSocketSession obj) {
		webSocketSessionMap.put(key, obj);

	}

	@Override
	public void remove(String key) {
		webSocketSessionMap.remove(key);
	}

}
