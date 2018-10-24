package com.framework.plugin.web.common.server.filter.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.framework.plugin.web.common.server.request.ServiceRequest;
import com.framework.plugin.web.common.server.response.ServiceResponse;

public class BasicAppFilter {

    public static String PASS = "1";
    public static String NOT_PASS = "0";

    public String execute(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            ServiceResponse<Object> sResponse, ServiceRequest request,
            String appCode, String appFunction) {
        return PASS;
    }

}
