package com.framework.plugin.web.common.server.tags.basic;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.utils.ReflectUtil;

@SuppressWarnings("serial")
public class BasicComponentDataTag extends BodyTagSupport {

    @Override
    public int doEndTag() throws JspException {

        try {
            JspWriter out = this.pageContext.getOut();
            JSONObject jsonObject = new JSONObject();
            Object object = this.getTagObject();

            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.getName().equals("serialVersionUID")) {
                    continue;
                }
                try {
                    Object fieldValue = ReflectUtil
                            .getFieldValue(object, field);
                    if (fieldValue != null) {
                        jsonObject.put(field.getName(), fieldValue.toString());
                    }
                } catch (SecurityException | InvocationTargetException
                        | NoSuchMethodException | IllegalArgumentException
                        | IllegalAccessException e) {
                    continue;
                }
            }
            out.print("<componentdata>" + jsonObject.toString()
                    + "</componentdata>");

            if (this.getBodyContent() != null) {
                out.print("<componentdatacontent>"
                        + this.getBodyContent().getString()
                        + "</componentdatacontent>");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.doEndTag();
    }

    public Object getTagObject() {
        return this;
    }
}
