package com.framework.plugin.web.system.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.plugin.web.common.bean.ConditionBean;
import com.framework.plugin.web.common.server.infs.ServletAwareInf;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.bean.ActInfoBean;
import com.framework.plugin.web.system.caches.AccountCache;

@Service
@Appcode(code = "AccountConfigService")
public class AccountConfigService implements ServletAwareInf {

    @Autowired
    private CRUDService crudService;

    @Autowired
    private AccountCache accountCache;

    private HttpSession session;

    @Appfunction(code = "doActPmsSave", paramNames = { "accountid",
            "permissionids" })
    public void doActPmsSave(String accountid, String permissionids) {
        CRUDRequest request = new CRUDRequest();

        ConditionBean condition = new ConditionBean();
        condition.setName("ACCOUNTID");
        condition.setValue(accountid);
        request.addCondition(condition);
        request.setTable("T_SYSTEM_ACT_PMS");
        crudService.del(request);

        if (StringUtils.isNotEmpty(permissionids)) {
            String[] permissionid = permissionids.split(",");
            for (String _permissionid : permissionid) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("ACCOUNTID", accountid);
                map.put("PERMISSIONID", _permissionid);
                request.addData(map);
            }
            crudService.add(request);
        }
    }

    /**
     * @param actInfo
     */
    @Appfunction(code = "doActInfoSave", paramNames = { "actInfo" })
    public void doActInfoSave(Map<String, String> actInfo) {
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_ACT_INFO");
        ConditionBean condition = new ConditionBean();
        condition.setName("ACCOUNTID");
        condition.setValue(actInfo.get("ACCOUNTID"));
        request.addCondition(condition);
        request.addData(actInfo);

        List<Map<String, String>> list = crudService.search(request);

        if (list != null && !list.isEmpty()) {
            crudService.edit(request);
        } else {
            crudService.add(request);
        }

        String tokenId = (String) session.getAttribute("tokenId");
        AccountBean account = accountCache.get(tokenId);
        ActInfoBean actInfoBean = new ActInfoBean();
        actInfoBean.setBirthday(actInfo.get("BIRTHDAY"));
        actInfoBean.setGender(actInfo.get("GENDER"));
        actInfoBean.setHeadimg(actInfo.get("HEADIMG"));
        actInfoBean.setNickname(actInfo.get("NICKNAME"));
        account.setActInfo(actInfoBean);
        accountCache.set(tokenId, account);
    }

    @Override
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Override
    public void setRequest(HttpServletRequest request) {

    }

    @Override
    public void setResponse(HttpServletResponse response) {
    }

}
