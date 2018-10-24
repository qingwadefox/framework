package com.framework.plugin.web.system.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.common.beans.ConditionBean;
import com.framework.common.utils.EncryptionUtil;
import com.framework.common.utils.RandomUtil;
import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.infs.ServletAwareInf;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.bean.ActInfoBean;
import com.framework.plugin.web.system.caches.AccountCache;

@Service
@Appcode(code = "LoginService")
public class LoginService implements ServletAwareInf {

    @Autowired
    private CRUDService crudService;

    @Autowired
    private AccountCache accountCache;

    private HttpSession session;

    private HttpServletRequest request;

    @Appfunction(code = "doLoginInfo")
    public AccountBean doLoginInfo() {
        String tokenId = (String) session.getAttribute("tokenId");
        return accountCache.get(tokenId);
    }

    @Appfunction(code = "doLogin", paramNames = { "name", "password" })
    public void doLogin(String name, String password) {
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_ACCOUNT");
        ConditionBean condition;

        // NAME
        condition = new ConditionBean();
        condition.setName("NAME");
        condition.setValue(name);
        request.addCondition(condition);

        // PASSWORD
        condition = new ConditionBean();
        condition.setName("PASSWORD");
        condition.setValue(password);
        request.addCondition(condition);

        List<Map<String, String>> list = crudService.search(request);
        if (list == null || list.isEmpty()) {
            throw new ServiceException(ExceptionEnum.LOGINFAILURE);
        } else {
            String ip = this.request.getRemoteAddr();
            String uuid = RandomUtil.getUUID().replace("-", "");
            String tokenId = EncryptionUtil.getMD5(ip) + "-" + uuid;
            session.setAttribute("tokenId", tokenId);
            AccountBean account = new AccountBean();
            Map<String, String> map = list.get(0);
            account.setId(map.get("ID"));
            account.setName(map.get("NAME"));
            account.setStatus(map.get("STATUS"));
            account.setTokenId(tokenId);
            
            request.setTable("T_SYSTEM_ACT_INFO");
            condition = new ConditionBean();
            condition.setName("ACCOUNTID");
            condition.setValue(account.getId());
            request.setCondition(condition);
            list = crudService.search(request);
            if (list != null && !list.isEmpty()) {
                map = list.get(0);
                ActInfoBean actInfo = new ActInfoBean();
                actInfo.setBirthday(map.get("BIRTHDAY"));
                actInfo.setGender(map.get("GENDER"));
                actInfo.setHeadimg(map.get("HEADIMG"));
                actInfo.setNickname(map.get("NICKNAME"));
                account.setActInfo(actInfo);
            }
            accountCache.set(tokenId, account);
            session.setAttribute("account", account);

        }
    }

    @Appfunction(code = "doLogout")
    public void doLogout() {
        String tokenId = (String) session.getAttribute("tokenId");
        accountCache.remove(tokenId);
        session.removeAttribute("tokenId");
    }

    @Override
    public void setSession(HttpSession session) {
        this.session = session;
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        this.request = request;

    }

    @Override
    public void setResponse(HttpServletResponse response) {

    }
}
