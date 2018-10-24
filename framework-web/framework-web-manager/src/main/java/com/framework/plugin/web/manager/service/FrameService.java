package com.framework.plugin.web.system.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.common.server.infs.ServletAwareInf;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.caches.MenuCache;

@Service
@Appcode(code = "FrameService")
public class FrameService implements ServletAwareInf {

    @Autowired
    private CRUDService CRUDService;

    @Autowired
    private MenuCache menuCache;

    @Autowired
    private AccountCache accountCache;

    private HttpSession session;

    @Appfunction(code = "doMenu")
    public List<Map<String, String>> doMenu() throws IOException,
            SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        String tokenId = (String) session.getAttribute("tokenId");
        AccountBean account = accountCache.get(tokenId);
        List<String> menuIdList = account.getMenuids();
        List<String> menuPidList = new ArrayList<String>();
        List<Map<String, String>> menuDataList = menuCache.getDatas();
        List<Map<String, String>> pmsMenuDataList = new ArrayList<Map<String, String>>();

        for (Map<String, String> menuData : menuDataList) {
            String id = menuData.get("ID");
            String pid = menuData.get("PID");
            if (menuIdList.contains(id) && !menuPidList.contains(pid)) {
                menuPidList.add(pid);
            }
        }

        if (menuIdList != null) {
            menuIdList.addAll(menuPidList);
            for (Map<String, String> menuData : menuDataList) {
                String id = menuData.get("ID");
                if (menuIdList.contains(id)) {
                    pmsMenuDataList.add(menuData);
                }
            }
        }

        return pmsMenuDataList;
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
