package com.framework.plugin.web.common.server.filter.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.response.CRUDResponse;

public class BasicCRUDFilter {
    public static String PASS = "1";
    public static String NOT_PASS = "0";
    public static String OP_SEARCH = "SEARCH";
    public static String OP_ADD = "ADD";
    public static String OP_DEL = "DEL";
    public static String OP_EDIT = "EDIT";

    public String execute(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            CRUDResponse cResponse, CRUDRequest cRequest, String opType) {
        return PASS;
    }

}
