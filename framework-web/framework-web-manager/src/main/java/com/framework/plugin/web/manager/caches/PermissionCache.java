package com.framework.plugin.web.system.caches;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Cachecode;
import com.framework.plugin.web.system.bean.PermissionBean;

@Cachecode(code = "PermissionCache")
public class PermissionCache implements CacheInf<PermissionBean> {

    @Autowired
    private CRUDService CRUDService;

    private Map<String, PermissionBean> datas;

    @Override
    public void create() {
        datas = new HashMap<String, PermissionBean>();
        List<Map<String, String>> result;
        CRUDRequest request = new CRUDRequest();

        // T_SYSTEM_PERMISSION
        request.setTable("T_SYSTEM_PERMISSION");
        result = CRUDService.search(request);
        for (Map<String, String> _map : result) {
            String id = _map.get("ID");
            String code = _map.get("CODE");
            PermissionBean pb = new PermissionBean();
            pb.setId(id);
            pb.setCode(code);
            datas.put(id, pb);
        }

        // T_SYSTEM_PMS_APP
        request.setTable("T_SYSTEM_PMS_APP");
        result = CRUDService.search(request);
        for (Map<String, String> _map : result) {
            String permissionid = _map.get("PERMISSIONID");
            String appcode = _map.get("APPCODE");
            String functioncode = _map.get("FUNCTIONCODE");
            PermissionBean pb = datas.get(permissionid);
            if (pb != null) {
                pb.addPmsApp(appcode, functioncode);
            }
        }

        // T_SYSTEM_PMS_MENU
        request.setTable("T_SYSTEM_PMS_MENU");
        result = CRUDService.search(request);
        for (Map<String, String> _map : result) {
            String permissionid = _map.get("PERMISSIONID");
            String menuid = _map.get("MENUID");
            PermissionBean pb = datas.get(permissionid);
            if (pb != null) {
                pb.addPmsMenu(menuid);
            }
        }

        // T_SYSTEM_PMS_TABLE
        request.setTable("T_SYSTEM_PMS_TABLE");
        result = CRUDService.search(request);
        for (Map<String, String> _map : result) {
            String permissionid = _map.get("PERMISSIONID");
            String tablename = _map.get("TABLENAME");
            String addable = _map.get("ADDABLE");
            String editable = _map.get("EDITABLE");
            String delable = _map.get("DELABLE");
            String searchable = _map.get("SEARCHABLE");
            PermissionBean pb = datas.get(permissionid);
            if (pb != null) {
                pb.addPmsTable(tablename, searchable, addable, editable,
                        delable);
            }
        }
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh() {
        this.create();
    }

    @Override
    public PermissionBean get(String key) {
        return datas.get(key);
    }

    @Override
    public void set(String key, PermissionBean obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(String key) {
        // TODO Auto-generated method stub
        
    }

}
