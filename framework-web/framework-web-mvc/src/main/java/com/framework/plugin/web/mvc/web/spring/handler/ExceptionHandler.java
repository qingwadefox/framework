package com.framework.plugin.web.common.web.spring.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.response.ServiceResponse;

public class ExceptionHandler extends SimpleMappingExceptionResolver {

    private static String ajaxHeader = "XMLHttpRequest";

    private class ExceptionResult {
        public String message;
        public String url;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }

    @SuppressWarnings("rawtypes")
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object object, Exception exception) {
        String _header = request.getHeader("X-Requested-With");
        boolean isAjax = _header == null || _header.equals(ajaxHeader) ? true
                : false;
        ExceptionResult exceptionResult = null;
        // if (exception.getClass().equals(InvocationTargetException.class)) {
        // exceptionResult = doServiceException((InvocationTargetException)
        // exception);
        // } else

        if (exception.getClass().equals(ServiceException.class)) {
            exceptionResult = doServiceException((ServiceException) exception);
        } else {
            exceptionResult = doException(exception);
        }

        if (isAjax) {
            ServiceResponse serviceResponse = new ServiceResponse();
            serviceResponse.setMessage(exceptionResult.getMessage());
            serviceResponse.setUrl(exceptionResult.getUrl());
            serviceResponse.setSuccess(false);
            serviceResponse.print(response);
        } else {

        }

        super.doResolveException(request, response, object, exception);
        return new ModelAndView();
    }

    // private ExceptionResult doServiceException(
    // InvocationTargetException exception) {
    // ServiceException _exception = (ServiceException) exception
    // .getTargetException();
    // ExceptionResult er = new ExceptionResult();
    // er.setMessage(_exception.getMessage());
    // er.setUrl(_exception.getUrl());
    // return er;
    // }

    private ExceptionResult doServiceException(ServiceException exception) {
        ExceptionResult er = new ExceptionResult();
        er.setMessage(exception.getMessage());
        er.setUrl(exception.getUrl());
        return er;
    }

    private ExceptionResult doException(Exception exception) {
        exception.printStackTrace();
        ExceptionResult er = new ExceptionResult();
        er.setMessage("系统级错误:" + exception.getClass().getName());
        return er;
    }
}
