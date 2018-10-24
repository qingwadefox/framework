package com.framework.plugin.web.system.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.framework.plugin.web.common.server.caches.WebSocketSessionCache;
import com.framework.plugin.web.common.server.infs.ServletAwareInf;
import com.framework.plugin.web.common.server.response.WebSocketResponse;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.caches.AccountCache;

@Service
@Appcode(code = "ChatService")
public class ChatService implements ServletAwareInf {

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private WebSocketSessionCache webSocketSessionCache;

    private HttpSession session;

    public void doChatOnline(AccountBean accountBean) {
        WebSocketResponse<Map<String, String>> response = new WebSocketResponse<Map<String, String>>();
        response.setCmd("chat-online");
        response.setSuccess(true);
        Map<String, String> result = new HashMap<String, String>();
        result.put("userId", accountBean.getId());
        result.put("headimg", accountBean.getActInfo().getHeadimg());
        if (accountBean.getActInfo() != null
                && StringUtils.isNotEmpty(accountBean.getActInfo()
                        .getNickname())) {
            result.put("name", accountBean.getActInfo().getNickname());
        } else {
            result.put("name", accountBean.getName());
        }
        response.setResult(result);

        for (AccountBean bean : accountCache.getAccountDatas().values()) {
            if (!bean.getId().equals(accountBean.getId())) {
                WebSocketSession session = webSocketSessionCache.get(bean
                        .getWssId());
                if (session != null) {
                    response.print(session);
                }
            }
        }
    }

    public void doChatList(AccountBean accountBean) {
        WebSocketResponse<List<Map<String, String>>> response = new WebSocketResponse<List<Map<String, String>>>();
        response.setCmd("chat-list");
        response.setSuccess(true);
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();

        for (AccountBean bean : accountCache.getAccountDatas().values()) {
            if (webSocketSessionCache.get(bean.getWssId()) != null) {
                Map<String, String> _map = new HashMap<String, String>();
                _map.put("userId", bean.getId());
                _map.put("headimg", bean.getActInfo().getHeadimg());

                if (bean.getActInfo() != null
                        && StringUtils.isNotEmpty(bean.getActInfo()
                                .getNickname())) {
                    _map.put("name", bean.getActInfo().getNickname());
                } else {
                    _map.put("name", bean.getName());
                }
                result.add(_map);
            }
        }
        response.setResult(result);
        response.print(webSocketSessionCache.get(accountBean.getWssId()));
    }

    public void doChatOffline(AccountBean accountBean) {

        WebSocketResponse<Map<String, String>> response = new WebSocketResponse<Map<String, String>>();
        response.setCmd("chat-offline");
        response.setSuccess(true);
        Map<String, String> result = new HashMap<String, String>();
        result.put("userId", accountBean.getId());
        response.setResult(result);

        for (AccountBean bean : accountCache.getAccountDatas().values()) {
            if (!bean.equals(accountBean)) {
                WebSocketSession session = webSocketSessionCache.get(bean
                        .getWssId());
                if (session != null && session.isOpen()) {
                    response.print(session);
                }
            }
        }
        accountBean.setWssId(null);
        accountCache.set(accountBean.getTokenId(), accountBean);
    }

    @Appfunction(code = "doChatSend", paramNames = { "userId", "message" })
    public void doChatSend(String userId, String message) {
        List<AccountBean> accountList = accountCache.getByUserId(userId);
        String tokenId = (String) session.getAttribute("tokenId");
        String accountId = accountCache.get(tokenId).getId();

        WebSocketResponse<Map<String, String>> response = new WebSocketResponse<Map<String, String>>();
        response.setCmd("chat-recmessage");
        response.setSuccess(false);
        Map<String, String> result = new HashMap<String, String>();
        result.put("userId", accountId);
        result.put("message", message);
        response.setResult(result);
        for (AccountBean _bean : accountList) {
            String wssid = _bean.getWssId();
            if (wssid != null && webSocketSessionCache.get(wssid) != null) {
                response.print(webSocketSessionCache.get(wssid));
            }
        }

    }

    @Override
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setResponse(HttpServletResponse response) {
        // TODO Auto-generated method stub

    }
}
