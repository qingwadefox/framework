package com.framework.plugin.web.system.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.bean.ConditionBean;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Cachecode;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.bean.PermissionBean;

@Cachecode(code = "AccountCache")
public class AccountCache implements CacheInf<AccountBean> {

    @Autowired
    private CRUDService crudService;

    @Autowired
    private PermissionCache permissionCache;

    private Map<String, AccountBean> accountDatas;

    @Override
    public void create() {
        accountDatas = new HashMap<String, AccountBean>();
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh() {
        Map<String, AccountBean> newTokenDatas = new HashMap<String, AccountBean>();
        Map<String, AccountBean> newIdDatas = new HashMap<String, AccountBean>();
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_ACCOUNT");

        Iterator<Entry<String, AccountBean>> it = accountDatas.entrySet()
                .iterator();

        while (it.hasNext()) {
            Entry<String, AccountBean> entry = it.next();
            ConditionBean condition = new ConditionBean();
            condition.setName("ID");
            condition.setValue(entry.getValue().getId());
            request.setCondition(condition);
            List<Map<String, String>> list = crudService.search(request);
            if (list != null && !list.isEmpty()) {
                Map<String, String> map = list.get(0);
                AccountBean bean = new AccountBean();
                bean.setId(map.get("ID"));
                bean.setName(map.get("NAME"));
                bean.setStatus(map.get("STATUS"));
                newTokenDatas.put(entry.getKey(), bean);
                newIdDatas.put(bean.getId(), bean);
            }
        }
        accountDatas = newTokenDatas;
        refershAllPermission();
    }

    public void refershAllPermission() {

        Iterator<String> it = accountDatas.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            refershPermission(key);
        }
    }

    public void refershPermission(String tokenId) {
        AccountBean account = accountDatas.get(tokenId);
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_ACT_PMS");
        ConditionBean condition = new ConditionBean();
        condition.setName("ACCOUNTID");
        condition.setValue(account.getId());
        request.setCondition(condition);
        List<Map<String, String>> list = crudService.search(request);
        List<String> pmsids = new ArrayList<String>();
        List<String> menuids = new ArrayList<String>();

        for (Map<String, String> _map : list) {
            String pmsid = _map.get("PERMISSIONID");
            pmsids.add(pmsid);
            PermissionBean permission = permissionCache.get(pmsid);
            List<String> _menuids = permission.getPmsMenuList();
            for (String _menuid : _menuids) {
                if (!menuids.contains(_menuid)) {
                    menuids.add(_menuid);
                }
            }

        }
        account.setPmsids(pmsids);
        account.setMenuids(menuids);
        accountDatas.put(tokenId, account);
    }

    @Override
    public AccountBean get(String key) {
        return accountDatas.get(key);
    }

    @Override
    public void set(String key, AccountBean obj) {
        accountDatas.put(key, obj);
        refershPermission(key);
    }

    @Override
    public void remove(String key) {
        accountDatas.remove(key);
    }

    public Map<String, AccountBean> getAccountDatas() {
        return accountDatas;
    }

    public AccountBean getByWebSocketSessionId(String sid) {
        for (AccountBean bean : accountDatas.values()) {
            if (bean.getWssId() != null && bean.getWssId().equals(sid)) {
                return bean;
            }
        }
        return null;
    }

    public List<AccountBean> getByUserId(String userId) {
        List<AccountBean> list = new ArrayList<AccountBean>();
        for (AccountBean bean : accountDatas.values()) {
            if (bean.getId().equals(userId)) {
                list.add(bean);
            }
        }
        return list;
    }
}
