package com.framework.plugin.web.common.web.spring.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.server.caches.WebSocketSessionCache;
import com.framework.plugin.web.common.server.infs.WebSocketInf;
import com.framework.plugin.web.common.web.spring.factory.SpringFactory;

public class DefaultWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private SpringFactory springFactory;

    @Autowired
    private WebSocketSessionCache webSocketSessionCache;

    @Override
    public void afterConnectionClosed(WebSocketSession session,
            CloseStatus status) throws Exception {
        List<WebSocketInf> list = springFactory.getWebSocketAppList();
        for (WebSocketInf wsApp : list) {
            wsApp.handleDisconnection(session, status);
        }
        webSocketSessionCache.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        List<WebSocketInf> list = springFactory.getWebSocketAppList();
        for (WebSocketInf wsApp : list) {
            wsApp.handleConnection(session);
        }
        webSocketSessionCache.set(session.getId(), session);
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session,
            TextMessage message) throws Exception {
        JSONObject data = JSONObject.parseObject(message.getPayload());
        List<WebSocketInf> list = springFactory.getWebSocketAppList();
        for (WebSocketInf wsApp : list) {
            wsApp.handleTextMessage(data, session);
        }
    }
}
