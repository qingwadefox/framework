package com.framework.plugin.web.system.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.common.utils.DateUtil;
import com.framework.plugin.web.common.bean.ConditionBean;
import com.framework.plugin.web.common.server.dao.SqlDao;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.response.TableResponse;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.common.web.spring.factory.SpringFactory;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.caches.PermissionCache;

@Service
@Appcode(code = "PermissionConfigService")
public class PermissionConfigService {

    @Autowired
    private PermissionCache permissionCache;

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private CRUDService crudService;

    @Autowired
    private SpringFactory springFactory;

    @Autowired
    private SqlDao sqlDao;

    @Appfunction(code = "refreshCache")
    public void refreshCache() {
        permissionCache.refresh();
        accountCache.refershAllPermission();
    }

    @Appfunction(code = "doEditMenu", paramNames = { "permissionid", "menuids" })
    public void doEditMenu(String permissionid, String menuids) {
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_PMS_MENU");
        ConditionBean cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.addCondition(cbean);
        crudService.del(request);

        if (StringUtils.isNotEmpty(menuids)) {
            request.setConditions(null);
            String[] menuid = menuids.split(",");

            for (String _menuid : menuid) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("PERMISSIONID", permissionid);
                map.put("MENUID", _menuid);
                map.put("ADDDATE", DateUtil.getNowDate());
                request.addData(map);
            }
            crudService.add(request);
        }
    }

    @Appfunction(code = "doEditApp", paramNames = { "permissionid", "apps" })
    public void doEditApp(String permissionid, List<Map<String, String>> apps) {
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_PMS_APP");
        ConditionBean cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.addCondition(cbean);
        crudService.del(request);

        if (apps != null && !apps.isEmpty()) {
            request.setConditions(null);

            for (Map<String, String> _app : apps) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("PERMISSIONID", permissionid);
                map.put("APPCODE", _app.get("APPCODE"));
                map.put("FUNCTIONCODE", _app.get("APPFUNCTION"));
                map.put("ADDDATE", DateUtil.getNowDate());
                request.addData(map);
            }
            crudService.add(request);
        }
    }

    @Appfunction(code = "doAppList")
    public List<Map<String, String>> doAppList() {
        List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

        List<Object> list = springFactory.getAppList();
        for (Object app : list) {
            Map<String, String> map = new HashMap<String, String>();
            Appcode appCode = app.getClass().getAnnotation(Appcode.class);
            map.put("PID", "0");
            map.put("ID", appCode.code());
            String name = appCode.code();
            if (StringUtils.isNotEmpty(appCode.description())) {
                name += "【" + appCode.description() + "】";
            }

            map.put("NAME", name);

            Method[] methods = app.getClass().getMethods();
            for (Method _method : methods) {
                Map<String, String> _map = new HashMap<String, String>();
                Appfunction appFunction = _method
                        .getAnnotation(Appfunction.class);
                if (appFunction != null) {
                    _map.put("PID", appCode.code());
                    _map.put("ID", appCode.code() + "-" + appFunction.code());
                    _map.put("APPCODE", appCode.code());
                    _map.put("APPFUNCTION", appFunction.code());
                    String _name = appFunction.code();
                    if (StringUtils.isNotEmpty(appFunction.description())) {
                        _name += "【" + appFunction.description() + "】";
                    }
                    _map.put("NAME", _name);
                    resultList.add(_map);
                }
            }

            resultList.add(map);
        }
        return resultList;
    }

    @Appfunction(code = "doTableList")
    public TableResponse doTableList() {
        TableResponse response = new TableResponse();
        String sql = "select distinct(table_name) from user_tab_columns";
        response.setDatas(sqlDao.select(sql));
        return response;
    }

    @Appfunction(code = "doDel", paramNames = { "permissionid" })
    public void doDel(String permissionid) {
        CRUDRequest request = new CRUDRequest();
        ConditionBean cbean;

        // 删除表T_SYSTEM_PERMISSION
        request.setTable("T_SYSTEM_PERMISSION");
        cbean = new ConditionBean();
        cbean.setName("ID");
        cbean.setValue(permissionid);
        request.setCondition(cbean);
        crudService.del(request);

        // 删除表T_SYSTEM_PMS_APP
        request.setTable("T_SYSTEM_PMS_APP");
        cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.setCondition(cbean);
        crudService.del(request);

        // 删除表T_SYSTEM_PMS_MENU
        request.setTable("T_SYSTEM_PMS_MENU");
        cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.setCondition(cbean);
        crudService.del(request);

        // 删除表T_SYSTEM_PMS_TABLE
        request.setTable("T_SYSTEM_PMS_TABLE");
        cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.setCondition(cbean);
        crudService.del(request);

    }

    @Appfunction(code = "doEditTable", paramNames = { "permissionid", "table",
            "field", "value" })
    public void doEditTable(String permissionid, String table, String field,
            String value) {
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_PMS_TABLE");
        ConditionBean cbean;

        // PERMISSIONID
        cbean = new ConditionBean();
        cbean.setName("PERMISSIONID");
        cbean.setValue(permissionid);
        request.addCondition(cbean);

        // TABLENAME
        cbean = new ConditionBean();
        cbean.setName("TABLENAME");
        cbean.setValue(table);
        request.addCondition(cbean);

        List<Map<String, String>> list = crudService.search(request);
        if (list == null || list.isEmpty()) {
            LinkedHashMap<String, String> addMap = new LinkedHashMap<String, String>();
            addMap.put("PERMISSIONID", permissionid);
            addMap.put("TABLENAME", table);
            addMap.put(field, value);
            addMap.put("ADDDATE", DateUtil.getNowDate());
            request.addData(addMap);
            crudService.add(request);
        } else {
            LinkedHashMap<String, String> editMap = new LinkedHashMap<String, String>();
            editMap.put(field, value);
            editMap.put("EDITDATE", DateUtil.getNowDate());
            request.addData(editMap);
            crudService.edit(request);
        }

    }
}
