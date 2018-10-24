package com.framework.plugin.web.common.web.spring.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.framework.common.utils.ReflectUtil;
import com.framework.plugin.web.common.server.filter.basic.BasicAppFilter;
import com.framework.plugin.web.common.server.filter.basic.BasicCRUDFilter;
import com.framework.plugin.web.common.server.infs.ServletAwareInf;
import com.framework.plugin.web.common.server.infs.WebSocketInf;
import com.framework.plugin.web.common.stereotype.AppFilter;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.CRUDFilter;
import com.framework.plugin.web.common.stereotype.WebSocketApp;
import com.framework.plugin.web.common.web.spring.utils.SpringUtil;

@Component
public class SpringFactory implements ApplicationContextAware,
        ServletContextAware {

    private ApplicationContext applicationContext;
    private ServletContext servletContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
        SpringUtil.applicationContext = applicationContext;

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        SpringUtil.servletContext = servletContext;
    }

    public Object getBean(Class<?> clazz) {
        return SpringUtil.getBean(clazz);
    }

    public Object getBean(String beanName) {
        return SpringUtil.getBean(beanName);
    }

    public Object setBean(String beanName, Object boject) {
        SpringUtil.setBean(beanName, boject);
        return SpringUtil.getBean(beanName);
    }

    public Object invokeMethod(String classPath, String classMethod,
            Map<String, Object> parameter) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException {

        Class<?> _class = Class.forName(classPath);
        Object app = this.getBean(_class);
        Method[] methods = ReflectUtil.getMethods(_class, classMethod,
                Appfunction.class);
        Object invokeResult = null;
        for (Method method : methods) {
            Appfunction appfunction = method.getAnnotation(Appfunction.class);

            // if (parameterSize == appfunction.paramNames().length) {
            List<Object> parameterList = new ArrayList<Object>();
            for (String paramName : appfunction.paramNames()) {
                parameterList.add(parameter.get(paramName));
            }
            invokeResult = method.invoke(app, parameterList.toArray());
            // }
        }
        return invokeResult;
    }

    public List<WebSocketInf> getWebSocketAppList() {
        Map<String, Object> beanMap = applicationContext
                .getBeansWithAnnotation(WebSocketApp.class);
        List<WebSocketInf> list = new ArrayList<WebSocketInf>();
        for (Object _app : beanMap.values()) {
            list.add((WebSocketInf) _app);
        }
        return list;
    }

    public Object invokeMethodApp(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession, String appCode,
            String appFunction, Map<String, Object> parameter)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Map<String, Object> beanMap = applicationContext
                .getBeansWithAnnotation(Appcode.class);
        Object app = null;
        for (Object _app : beanMap.values()) {
            Appcode _appcode = _app.getClass().getAnnotation(Appcode.class);
            if (_appcode.code().equals(appCode)) {
                app = _app;
            }
        }
        if (ReflectUtil.existInterfaces(app.getClass(), ServletAwareInf.class)) {
            ((ServletAwareInf) app).setResponse(hResponse);
            ((ServletAwareInf) app).setRequest(hRequest);
            ((ServletAwareInf) app).setSession(hSession);
        }

        Method[] methods = ReflectUtil.getMethods(app.getClass(),
                Appfunction.class);
        Object invokeResult = null;
        for (Method method : methods) {
            Appfunction _appfunction = method.getAnnotation(Appfunction.class);

            if (_appfunction.code().equals(appFunction)) {
                List<Object> parameterList = new ArrayList<Object>();
                // Class<?>[] _paramTypes = method.getParameterTypes();
                // int i = 0;
                for (String paramName : _appfunction.paramNames()) {
                    // Object _parameter = TypeUtil.toObject(
                    // parameter.get(paramName), _paramTypes[i]);
                    // Object _parameter = TypeUtil.toObject(),
                    // _paramTypes[i].getClass());
                    parameterList.add(parameter.get(paramName));
                    // i++;
                }
                invokeResult = method.invoke(app, parameterList.toArray());
            }
        }
        return invokeResult;
    }

    public List<Object> getAppList() {
        Map<String, Object> beanMap = applicationContext
                .getBeansWithAnnotation(Appcode.class);
        List<Object> list = new ArrayList<Object>();
        for (Object _app : beanMap.values()) {
            list.add(_app);
        }
        return list;
    }

    public List<BasicAppFilter> getAppFilterList() {
        Map<String, Object> beanMap = applicationContext
                .getBeansWithAnnotation(AppFilter.class);
        List<BasicAppFilter> list = new ArrayList<BasicAppFilter>();
        for (Object filter : beanMap.values()) {
            if (ReflectUtil.existSuperClasses(filter.getClass(),
                    BasicAppFilter.class))
                list.add((BasicAppFilter) filter);
        }
        return list;
    }

    public List<BasicCRUDFilter> getCRUDFilterList() {
        Map<String, Object> beanMap = applicationContext
                .getBeansWithAnnotation(CRUDFilter.class);
        List<BasicCRUDFilter> list = new ArrayList<BasicCRUDFilter>();
        for (Object filter : beanMap.values()) {
            if (ReflectUtil.existSuperClasses(filter.getClass(),
                    BasicCRUDFilter.class))
                list.add((BasicCRUDFilter) filter);
        }
        return list;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

}
