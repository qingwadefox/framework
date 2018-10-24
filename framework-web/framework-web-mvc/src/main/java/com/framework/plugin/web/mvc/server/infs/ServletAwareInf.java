package com.framework.plugin.web.common.server.infs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface ServletAwareInf {

    public void setSession(HttpSession session);

    public void setRequest(HttpServletRequest request);

    public void setResponse(HttpServletResponse response);
}
