package com.framework.plugin.web.system.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.filter.basic.BasicAppFilter;
import com.framework.plugin.web.common.server.request.ServiceRequest;
import com.framework.plugin.web.common.server.response.ServiceResponse;
import com.framework.plugin.web.common.stereotype.AppFilter;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.bean.PermissionBean;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.caches.PermissionCache;

@AppFilter(code = "PmsAppFilter", excludeApps = { "LoginService" })
public class PmsAppFilter extends BasicAppFilter {

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private PermissionCache permissionCache;

    @Override
    public String execute(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            ServiceResponse<Object> sResponse, ServiceRequest request,
            String appCode, String appFunction) {
        boolean pass = false;
        String tokenId = (String) hSession.getAttribute("tokenId");
        AccountBean account = accountCache.get(tokenId);

        if (account != null && account.getPmsids() != null
                && !account.getPmsids().isEmpty()) {
            List<String> pmsids = account.getPmsids();
            for (String pmsid : pmsids) {
                PermissionBean permission = permissionCache.get(pmsid);
                if (permission != null
                        && permission.checkPmsApp(appCode, appFunction)) {
                    pass = true;
                    break;

                }
            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.POWERUNKNOW);
        }

        return super.execute(hResponse, hRequest, hSession, sResponse, request,
                appCode, appFunction);

    }
}
