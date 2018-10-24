package com.framework.plugin.web.system.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.utils.EncryptionUtil;
import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.filter.basic.BasicAppFilter;
import com.framework.plugin.web.common.server.request.ServiceRequest;
import com.framework.plugin.web.common.server.response.ServiceResponse;
import com.framework.plugin.web.common.stereotype.AppFilter;
import com.framework.plugin.web.system.caches.AccountCache;

@AppFilter(code = "LoginFilter", excludeApps = { "LoginService" })
public class LoginFilter extends BasicAppFilter {

    @Autowired
    private AccountCache accountCache;

    @Override
    public String execute(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            ServiceResponse<Object> sResponse, ServiceRequest request,
            String appCode, String appFunction) {
        String tokenId = (String) hSession.getAttribute("tokenId");
        if (StringUtils.isEmpty(tokenId)) {
            throw new ServiceException(ExceptionEnum.LOGINUNKNOW,
                    "/system/login/index.jsp");
        }

        String ip = hRequest.getRemoteAddr();
        String md5Ip = EncryptionUtil.getMD5(ip);
        String tokenMd5Ip = tokenId.split("-")[0];
        if (!md5Ip.equals(tokenMd5Ip)) {
            throw new ServiceException(ExceptionEnum.LOGINUNKNOW,
                    "/system/login/index.jsp");
        }

        if (accountCache.get(tokenId) == null) {
            throw new ServiceException(ExceptionEnum.LOGINUNKNOW,
                    "/system/login/index.jsp");
        }

        return super.execute(hResponse, hRequest, hSession, sResponse, request,
                appCode, appFunction);
    }
}
