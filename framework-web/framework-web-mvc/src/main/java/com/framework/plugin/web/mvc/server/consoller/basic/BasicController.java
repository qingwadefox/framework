package com.framework.plugin.web.common.server.consoller.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.request.ServiceRequest;
import com.framework.plugin.web.common.server.response.CRUDResponse;
import com.framework.plugin.web.common.server.response.ServiceResponse;
import com.framework.plugin.web.common.server.response.basic.BasicResponse;

public class BasicController {

    @Autowired
    private HttpServletRequest request;

    public <T> ServiceResponse<T> getServiceResponse(ServiceRequest request) {
        ServiceResponse<T> response = new ServiceResponse<T>();
        response.setMode(request.getMode());
        response.setSuccess(true);
        return response;
    }

    public <T> ServiceResponse<T> getServiceResponse() {
        ServiceResponse<T> response = new ServiceResponse<T>();
        response.setMode(BasicResponse.WEB_MODE);
        response.setSuccess(true);
        return response;
    }

    public CRUDResponse getCRUDResponse(CRUDRequest request) {
        CRUDResponse response = new CRUDResponse();
        response.setMode(request.getMode());
        response.setSuccess(true);
        return response;
    }

    public void print(HttpServletResponse hResponse,
            ServiceResponse<?> sResponse) {
        sResponse.print(hResponse);
    }

    //
    // public CRUDResponse getCRUDResponse() {
    // return new CRUDResponse();
    // }
    //
    // public <T> ServiceResponse<T> getServiceResponse(ServiceRequest request)
    // {
    // ServiceResponse<T> response = new ServiceResponse<T>();
    // response.setMessage("操作成功");
    // response.setMode(request.getMode());
    // response.setSuccess(true);
    // return response;
    // }
    //
    // public <T> ServiceResponse<T> getServiceResponse() {
    // return new ServiceResponse<T>();
    // }
    //

    //
    // public EntityResponse getEntityResponse() {
    // return new EntityResponse();
    // }

    // /**
    // * ajax成功信息
    // *
    // * @param response
    // * @param basicResponse
    // */
    // public void printSuccess(HttpServletResponse response,
    // BasicResponse basicResponse) {
    // if (basicResponse == null) {
    // basicResponse = this.getBasicResponse();
    // }
    // if (basicResponse.getMessage() == null) {
    // basicResponse.setMessage("操作成功!");
    // }
    // basicResponse.setSuccess(true).print(response);
    // }
    //
    // /**
    // * ajax成功信息
    // *
    // * @param response
    // * @param basicResponse
    // */
    // public void printSuccess(HttpServletResponse response) {
    // this.printSuccess(response, null);
    // }

    // /**
    // * ajaxobject
    // *
    // * @param response
    // * @param object
    // */
    // public void printObject(HttpServletResponse response, Object object) {
    // this.getBasicResponse().setMessage("操作成功!").setResult(object)
    // .setSuccess(true).print(response);
    // }
    //
    // /**
    // * ajax失败信息
    // *
    // * @param response
    // * @param e
    // */
    // public void printFailure(HttpServletResponse response, Exception e) {
    // this.getCRUDResponse().setSuccess(false).setMessage(e.getMessage())
    // .print(response);
    //
    // }

    /**
     * 获取参数
     * 
     * @param name
     * @return
     */
    public String getParameter(String name) {
        String parameter = this.getRequest().getParameter(name);
        if (parameter == null) {
            String[] parameters = this.getRequest().getParameterMap().get(name);
            if (parameters != null && parameters.length > 0) {
                parameter = parameters[0];
            }
        }
        return parameter;
    }

    /**
     * 获取参数组
     * 
     * @param name
     * @return
     */
    public String[] getParameters(String name) {
        return this.getRequest().getParameterValues(name + "[]");
    }

    /**
     * 设置参数
     * 
     * @param name
     * @param value
     */
    public BasicController setAttribute(String name, Object value) {
        this.getRequest().setAttribute(name, value);
        return this;
    }

    /**
     * 获取request
     * 
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

}
