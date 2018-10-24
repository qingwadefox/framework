package com.framework.plugin.web.common.server.tags.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.utils.ListUtil;
import com.framework.plugin.web.common.web.spring.utils.SpringUtil;
import com.framework.plugin.web.system.caches.MenuCache;

public class PheaderTag extends BodyTagSupport {

    private static final long serialVersionUID = 750515171493799194L;

    private String name;
    private String remark;

    @Override
    public int doStartTag() throws JspException {
        String name = "";
        String remark = "";
        List<String> child = new ArrayList<String>();

        if (StringUtils.isEmpty(this.name)) {
            String id = pageContext.getRequest().getParameter("mid");
            MenuCache cache = (MenuCache) SpringUtil.getCache("menucache");
            List<Map<String, String>> list = cache.getDatas();
            String pid = "0";
            for (Map<String, String> map : list) {
                if (map.get("ID").equals(id)) {
                    pid = map.get("PID");
                    child.add("<li><a href=\"#\">" + map.get("NAME")
                            + "</a></li>");
                    name = map.get("NAME");
                    remark = map.get("REMARK");
                }
            }
            while (!pid.equals("0")) {
                for (Map<String, String> map : list) {
                    if (map.get("ID").equals(pid)) {
                        pid = map.get("PID");
                        child.add("<li><a href=\"#\"> "
                                + map.get("NAME")
                                + " </a><i class=\"fa fa-angle-right\"></i></li>");
                    }
                }
            }
            child = ListUtil.reverse(child);
        } else {
            name = this.name;
            remark = this.remark;
            child.add("<li><a href=\"#\">" + name + "</a></li>");
        }

        JspWriter out = this.pageContext.getOut();
        try {
            out.print("<div class=\"page-content pheader\">"
                    + "<h3 class=\"page-title\">"
                    + name
                    + " <small>"
                    + remark
                    + "</small>"
                    + "</h3>"
                    + " <div class=\"page-bar\"><ul class=\"page-breadcrumb\">"
                    + " <li><i class=\"fa fa-home\"></i> <a href=\"index.html\" tppabs=\"\"> "
                    + "主页 </a> <i class=\"fa fa-angle-right\"></i></li>"
                    + StringUtils.join(child, "") + "  </ul></div>" + " </div>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
