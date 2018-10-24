package com.framework.plugin.web.system.wsservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;
import com.framework.plugin.web.common.server.caches.WebSocketSessionCache;
import com.framework.plugin.web.common.server.infs.WebSocketInf;
import com.framework.plugin.web.common.server.response.WebSocketResponse;
import com.framework.plugin.web.common.stereotype.WebSocketApp;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.service.ChatService;

@WebSocketApp
public class LoginWSService implements WebSocketInf {

    private Map<String, TimerTask> logoutMap = new HashMap<String, TimerTask>();

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private WebSocketSessionCache webSocketSessionCache;

    @Autowired
    private ChatService chatService;

    @Override
    public void handleTextMessage(JSONObject data, WebSocketSession session) {

        String tokenId = data.getString("tokenId");

        // 如果TOKENID为空就不判断
        if (StringUtils.isEmpty(tokenId)) {
            return;
        }

        // 根据token获取用户ID
        AccountBean accountBean = accountCache.get(tokenId);

        // 获取重复登录的用户token（存在websocket链接的）
        List<String> repeatTokenList = this.getRepeatTokenList(accountBean);

        // 不能存在TOKENID：判定操作为新登录用户
        if (repeatTokenList.isEmpty()) {
            chatService.doChatOnline(accountBean);
        }
        // 只存在一个TOKENID并且判定TOKENID为自己：判定操作为刷新页面
        else if (repeatTokenList.size() == 1 && repeatTokenList.get(0).equals(tokenId)) {
            // 由于刷新页面会注销websocket并且执行登出操作，所以删除登出操作的任务，并且给用户赋予新的websocketid
            if (logoutMap.get(tokenId) != null) {
                logoutMap.get(tokenId).cancel();
                logoutMap.remove(tokenId);
            }
        }
        // 存在多个TOKENID（必定为多客户端登录）：判定操作为重复登录
        else {
            List<String> webSocketIds = new ArrayList<String>();
            for (String _tokenId : repeatTokenList) {
                // 过滤掉自身的，获取其他登录用户的websocketid
                if (!_tokenId.equals(tokenId)) {
                    AccountBean _bean = accountCache.get(_tokenId);
                    if (_bean.getWssId() != null) {
                        // 获取到websocketID
                        webSocketIds.add(_bean.getWssId());
                        // 注销掉websocketID
                        _bean.setWssId(null);
                    }
                }
            }

            if (!webSocketIds.isEmpty()) {
                @SuppressWarnings("rawtypes")
                WebSocketResponse response = new WebSocketResponse();
                response.setCmd("login-logout");
                response.setMessage("用户已经在其他地方登录");
                response.setSuccess(false);
                for (String wssId : webSocketIds) {
                    response.print(webSocketSessionCache.get(wssId));
                }
            }
        }

        accountBean.setWssId(session.getId());
        chatService.doChatList(accountBean);
    }

    /**
     * 获取重复登录的用户信息
     * 
     * @return
     */
    public List<String> getRepeatTokenList(AccountBean accountBean) {
        List<AccountBean> loginAccountList = accountCache
                .getByUserId(accountBean.getId());

        List<String> repeatTokenList = new ArrayList<String>();

        for (AccountBean _accountBean : loginAccountList) {
            if (_accountBean.getWssId() != null) {
                repeatTokenList.add(_accountBean.getTokenId());
            }
        }
        return repeatTokenList;
    }

    @Override
    public void handleConnection(WebSocketSession session) {

    }

    @Override
    public void handleDisconnection(WebSocketSession session, CloseStatus status) {
        final AccountBean bean = accountCache.getByWebSocketSessionId(session
                .getId());
        if (bean != null) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    chatService.doChatOffline(bean);
                    this.cancel();
                }
            };
            logoutMap.put(bean.getTokenId(), timerTask);
            new Timer().schedule(timerTask, 2000);
        }

    }
}
