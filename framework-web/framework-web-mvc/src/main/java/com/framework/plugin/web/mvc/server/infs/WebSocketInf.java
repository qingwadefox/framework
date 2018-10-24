package com.framework.plugin.web.common.server.infs;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;

public interface WebSocketInf {

    public void handleConnection(WebSocketSession session);

    public void handleDisconnection(WebSocketSession session, CloseStatus status);

    public void handleTextMessage(JSONObject data, WebSocketSession session);
}
