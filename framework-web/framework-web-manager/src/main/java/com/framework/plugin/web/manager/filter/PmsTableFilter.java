package com.framework.plugin.web.system.filter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.filter.basic.BasicCRUDFilter;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.response.CRUDResponse;
import com.framework.plugin.web.common.stereotype.CRUDFilter;
import com.framework.plugin.web.system.bean.AccountBean;
import com.framework.plugin.web.system.bean.PermissionBean;
import com.framework.plugin.web.system.caches.AccountCache;
import com.framework.plugin.web.system.caches.PermissionCache;

@CRUDFilter(code = "PmsTableFilter")
public class PmsTableFilter extends BasicCRUDFilter {

    @Autowired
    private AccountCache accountCache;

    @Autowired
    private PermissionCache permissionCache;

    @Override
    public String execute(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            CRUDResponse cResponse, CRUDRequest cRequest, String opType) {

        boolean pass = false;
        String tokenId = (String) hSession.getAttribute("tokenId");
        AccountBean account = accountCache.get(tokenId);

        if (account != null && !account.getPmsids().isEmpty()) {
            List<String> pmsids = account.getPmsids();
            for (String pmsid : pmsids) {
                PermissionBean permission = permissionCache.get(pmsid);
                String tablename = cRequest.getTable();

                if (permission != null) {
                    if (opType.equals(OP_ADD)
                            && permission.checkPmsTableAddable(tablename)) {
                        pass = true;
                        break;
                    } else if (opType.equals(OP_DEL)
                            && permission.checkPmsTableDelable(tablename)) {
                        pass = true;
                        break;
                    } else if (opType.equals(OP_EDIT)
                            && permission.checkPmsTableEditable(tablename)) {
                        pass = true;
                        break;
                    } else if (opType.equals(OP_SEARCH)
                            && permission.checkPmsTableSearchable(tablename)) {
                        pass = true;
                        break;
                    }
                }

            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.POWERUNKNOW);
        }
        return super.execute(hResponse, hRequest, hSession, cResponse,
                cRequest, opType);

    }
}
