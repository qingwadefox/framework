package com.framework.plugin.web.system.caches;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Cachecode;

@Cachecode(code = "menucache")
public class MenuCache implements CacheInf<Map<String, String>> {

    @Autowired
    private CRUDService CRUDService;

    private List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

    public List<Map<String, String>> getDatabaseDatas() {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_MENU");
        datas.addAll(CRUDService.search(request));
        return datas;
    }

    @Override
    public void create() {
        datas.clear();
        datas.addAll(getDatabaseDatas());
    }

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    @Override
    public void destroy() {
        datas.clear();

    }

    @Override
    public void refresh() {
        this.create();

    }

    @Override
    public Map<String, String> get(String key) {
        return null;
    }

    @Override
    public void set(String key, Map<String, String> obj) {

    }

    @Override
    public void remove(String key) {
        // TODO Auto-generated method stub
        
    }

}
