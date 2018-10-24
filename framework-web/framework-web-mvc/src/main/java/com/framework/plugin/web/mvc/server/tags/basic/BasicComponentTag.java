package com.framework.plugin.web.common.server.tags.basic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.utils.ReflectUtil;

@SuppressWarnings("serial")
public class BasicComponentTag extends BodyTagSupport {

    private List<String> paramList;

    public void doTag() throws IllegalArgumentException, IllegalAccessException {
        paramList = new ArrayList<String>();
        Object object = this.getTagObject();
        for (Field field : object.getClass().getDeclaredFields()) {

            if (field.getName().equals("serialVersionUID")) {
                continue;
            }

            try {
                Object fieldValue = ReflectUtil.getFieldValue(object, field);
                if (fieldValue != null) {
                    paramList.add(field.getName() + "=\""
                            + fieldValue.toString() + "\" ");
                }
            } catch (SecurityException | InvocationTargetException
                    | NoSuchMethodException e) {
                continue;
            }

        }
    }

    public void destroy() {
    }

    public String getType() {
        return null;
    }

    public Object getTagObject() {
        return null;
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            doTag();
        } catch (IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }

        String bodyContent = "";

        if (this.getBodyContent() != null) {
            bodyContent = "<componentcontent>"
                    + this.getBodyContent().getString() + "</componentcontent>";
        }

        try {
            JspWriter out = this.pageContext.getOut();
            out.print("<component type=\"" + this.getType() + "\" "
                    + StringUtils.join(paramList, " ") + ">" + bodyContent
                    + "</component>");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }

    @Override
    public int doStartTag() throws JspException {
        return super.doStartTag();
    }
}
