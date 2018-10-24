package com.framework.plugin.web.system.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.system.caches.MenuCache;

@Service
@Appcode(code = "MenuConfigService")
public class MenuConfigService {

    @Autowired
    private MenuCache menuCache;

    @Appfunction(code = "doList")
    public List<Map<String, String>> doList() throws IOException,
            SecurityException, NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        List<Map<String, String>> menuList = new ArrayList<Map<String, String>>();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ID", "0");
        map.put("NAME", "所有菜单");
        map.put("PID", "-1");
        map.put("TYPE", "0");

        menuList.add(map);
        menuList.addAll(menuCache.getDatabaseDatas());

        return menuList;
    }

    @Appfunction(code = "doSearchList", paramNames = { "pid" })
    public List<Map<String, String>> doSearchList(String pid) {
        pid = pid == null ? "0" : pid;
        List<Map<String, String>> menuList = new ArrayList<Map<String, String>>();

        for (Map<String, String> map : menuCache.getDatabaseDatas()) {
            if (map.get("PID").equals(pid)) {
                menuList.add(map);
            }
        }

        return menuList;

    }

    @Appfunction(code = "refreshCache")
    public void refreshCache() {
        menuCache.refresh();
    }

    @Appfunction(code = "doDel")
    public void doDel() {

    }

}
