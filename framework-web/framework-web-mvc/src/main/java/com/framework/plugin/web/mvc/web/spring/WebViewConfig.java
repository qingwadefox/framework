package com.framework.plugin.web.common.web.spring;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Component
public class WebViewConfig extends InternalResourceViewResolver {

    @Override
    protected Class<?> getViewClass() {
        return org.springframework.web.servlet.view.JstlView.class;
    }

    @Override
    protected String getPrefix() {
        return "/";
    }

    @Override
    protected String getSuffix() {
        return ".jsp";
    }

}
