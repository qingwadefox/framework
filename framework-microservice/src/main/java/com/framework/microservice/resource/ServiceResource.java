/**
 * Copyright (c) 1987-2010 Fujian Fujitsu Communication Software Co., 
 * Ltd. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of 
 * Fujian Fujitsu Communication Software Co., Ltd. 
 * ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with 
 * the terms of the license agreement you entered into with FFCS.
 *
 * FFCS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. FFCS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.framework.microservice.resource;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.spi.HttpRequest;

import com.alibaba.fastjson.JSONObject;
import com.framework.common.log.ILogger;
import com.framework.common.log.LoggerFactory;
import com.framework.common.utils.DateUtil;
import com.framework.microservice.ServiceMap;
import com.framework.microservice.ServiceResult;
import com.framework.microservice.annotations.Register;
import com.framework.microservice.annotations.Service;
import com.framework.microservice.context.Constants;
import com.framework.microservice.context.ServiceResponse;
import com.framework.microservice.factory.SecurityFactory;
import com.framework.microservice.statistics.ServiceAccessStatistics;
import com.framework.microservice.utils.ServiceUtil;

/**
 * .
 * 
 * @版权：福富软件 版权所有 (c) 2015
 * @author zhengwei3@ffcs.cn
 * @version Revision 1.0.0
 * @see:
 * @创建日期：2017年3月17日
 * @功能说明：
 * 
 */
@Path("/")
public class ServiceResource {
	private static final ILogger logger = LoggerFactory.getLogger(ServiceResource.class);

	private String serviceResourceClassName;
	private Object serviceResource;
	private Map<String, Method> methodMap;
	private List<String> annotationsPathList;
	private String serviceResourceRegisterValue;

	public ServiceResource(Class<?> serviceClass) throws Exception {

		serviceResourceClassName = serviceClass.getName();
		serviceResource = serviceClass.newInstance();
		methodMap = new HashMap<String, Method>();
		annotationsPathList = new ArrayList<String>();
		Method[] methods = serviceClass.getMethods();
		serviceResourceRegisterValue = serviceClass.getAnnotation(Register.class).value();
		serviceResourceRegisterValue = (StringUtils.isNotEmpty(serviceResourceRegisterValue) && !serviceResourceRegisterValue.startsWith("/")) ? "/" + serviceResourceRegisterValue : serviceResourceRegisterValue;
		for (Method _method : methods) {
			Service service = _method.getAnnotation(Service.class);
			if (service == null) {
				continue;
			}

			if (service.params().length != service.paramsdetail().length) {
				throw new Exception("服务方法中的参数名与参数说明数量不匹配");
			}

			Class<?>[] methodParamTypes = _method.getParameterTypes();

			if (methodParamTypes != null && methodParamTypes.length > 0) {
				for (Class<?> methodParamType : methodParamTypes) {
					if (!methodParamType.equals(ServiceMap.class) && !methodParamType.equals(ServiceResponse.class)) {
						throw new Exception("服务方法的参数只能用【ServiceResponse】或者【ServiceMap】为参数");
					}
				}
			}

			Class<?> methodReturnType = _method.getReturnType();
			if (methodReturnType != null && !methodReturnType.equals(Void.class) && !methodReturnType.equals(ServiceResult.class)) {
				throw new Exception("服务方法的返回参数只能用【无参数】或者【ServiceResult<T>】做为返回参数");
			}
			if (methodMap.containsKey(_method.getName())) {
				throw new Exception("已经存在方法【" + _method.getName() + "】作为服务，不能使用相同名称的方法");
			}

			methodMap.put(_method.getName(), _method);

			if (StringUtils.isNotEmpty(serviceResourceRegisterValue) && StringUtils.isNotEmpty(service.value())) {
				String fullPath = ServiceUtil.getAnnotationsPath(serviceResourceRegisterValue, service.value());
				if (annotationsPathList.contains(fullPath)) {
					throw new Exception("已经存在以URL【" + fullPath + "】作为服务的方法，不能使用相同的路径配置多个方法");
				}
				annotationsPathList.add(fullPath);
				methodMap.put(fullPath, _method);
			}

		}

		if (methodMap.size() == 0) {
			throw new Exception("不存在需要开放的服务");
		}
	}
	@GET
	@Produces("text/plain; charset=utf-8")
	public Response doGet(@Context ChannelHandlerContext ctx, @Context HttpRequest request, @HeaderParam("SERVICE-ID") String serviceId, @HeaderParam("SERVICE-STEP") Integer serviceStep) {
		logger.debug("获取到【get】请求,请求时间为【", DateUtil.getNowDate(), "】!");
		String visitPath = request.getUri().getPath();
		String methodName = visitPath.replaceFirst("/" + Constants.PREFIX_SERVICE, "");
		// 判断服务是否存在
		if (!methodMap.containsKey(methodName)) {
			ServiceResult<Void> result = new ServiceResult<Void>();
			result.setErrorCode(404);
			result.setErrorMsg("page not found");
			return Response.status(Status.OK).entity(JSONObject.toJSONString(result)).build();
		}
		// 开始服务入参操作
		return Response.status(Status.OK).entity(JSONObject.toJSONString(invokeServiceMethod(methodName, request.getUri().getQueryParameters(), null, serviceId, serviceStep, ServiceUtil.getIp(ctx)))).build();
	}

	@POST
	@Consumes({MediaType.MEDIA_TYPE_WILDCARD})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response doPost(@Context ChannelHandlerContext ctx, @Context HttpRequest request, @HeaderParam("SERVICE-ID") String serviceId, @HeaderParam("SERVICE-STEP") Integer serviceStep) {
		logger.debug("获取到【post】请求,请求时间为【", DateUtil.getNowDate(), "】!");
		String visitPath = request.getUri().getPath();
		String methodName = visitPath.replaceFirst("/" + Constants.PREFIX_SERVICE, "");
		if (!methodMap.containsKey(methodName)) {
			ServiceResult<Void> result = new ServiceResult<Void>();
			result.setErrorCode(404);
			result.setErrorMsg("page not found");
			return Response.status(Status.OK).entity(JSONObject.toJSONString(result)).build();
		}
		// 开始服务入参操作
		return Response.status(Status.OK).entity(JSONObject.toJSONString(invokeServiceMethod(methodName, request.getUri().getQueryParameters(), request.getInputStream(), serviceId, serviceStep, ServiceUtil.getIp(ctx)))).build();
	}
	@GET
	@Path("/{mehod}")
	@Produces("text/plain; charset=utf-8")
	public Response doMethodGet(@Context ChannelHandlerContext ctx, @PathParam("mehod") PathSegment mehod, @Context HttpRequest request, @HeaderParam("SERVICE-ID") String serviceId, @HeaderParam("SERVICE-STEP") Integer serviceStep) {
		logger.debug("获取到【methodget】请求,请求时间为【", DateUtil.getNowDate(), "】!");
		String methodName = mehod.getPath();
		// 判断服务是否存在
		if (!methodMap.containsKey(methodName)) {
			ServiceResult<Void> result = new ServiceResult<Void>();
			result.setErrorCode(404);
			result.setErrorMsg("page not found");
			return Response.status(Status.OK).entity(JSONObject.toJSONString(result)).build();
		}
		// 开始服务入参操作
		return Response.status(Status.OK).entity(JSONObject.toJSONString(invokeServiceMethod(methodName, request.getUri().getQueryParameters(), null, serviceId, serviceStep, ServiceUtil.getIp(ctx)))).build();
	}

	@POST
	@Path("/{mehod}")
	@Produces("text/plain; charset=utf-8")
	public Response doMethodPost(@Context ChannelHandlerContext ctx, @PathParam("mehod") PathSegment mehod, @Context HttpRequest request, @HeaderParam("SERVICE-ID") String serviceId, @HeaderParam("SERVICE-STEP") Integer serviceStep) {
		logger.debug("获取到【methodpost】请求,请求时间为【", DateUtil.getNowDate(), "】!");
		String methodName = mehod.getPath();
		if (!methodMap.containsKey(methodName)) {
			ServiceResult<Void> result = new ServiceResult<Void>();
			result.setErrorCode(404);
			result.setErrorMsg("page not found");
			return Response.status(Status.OK).entity(JSONObject.toJSONString(result)).build();
		}
		// 开始服务入参操作
		return Response.status(Status.OK).entity(JSONObject.toJSONString(invokeServiceMethod(methodName, request.getUri().getQueryParameters(), request.getInputStream(), serviceId, serviceStep, ServiceUtil.getIp(ctx)))).build();
	}
	private ServiceResult<?> invokeServiceMethod(String methodName, MultivaluedMap<String, String> map, InputStream requestInputStream, String serviceId, Integer serviceStep, String ip) {
		Method method = methodMap.get(methodName);
		ServiceAccessStatistics.add(serviceResourceClassName + "." + method.getName(), ip);

		if (!SecurityFactory.checkSecurity(ip, serviceResourceClassName + "." + method.getName())) {
			ServiceResult<Void> result = new ServiceResult<Void>();
			result.setErrorCode(401);
			result.setErrorMsg("unauthorized ip");
			return result;
		}

		ServiceResponse response = null;
		Object[] paramObjs = null;
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes != null && parameterTypes.length > 0) {
			paramObjs = new Object[parameterTypes.length];
			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> parameterType = parameterTypes[i];
				if (parameterType.equals(ServiceMap.class)) {
					paramObjs[i] = new ServiceMap<String, String>((MultivaluedMapImpl<String, String>) map, requestInputStream);
				} else if (parameterType.equals(ServiceResponse.class)) {
					if (response == null) {
						response = new ServiceResponse();
						if (StringUtils.isEmpty(serviceId)) {
							serviceId = UUID.randomUUID().toString();
						}
						if (serviceStep == null) {
							serviceStep = 0;
						}
						serviceStep += 1;
						response.setServiceId(serviceId);
						response.setServiceStep(serviceStep);
						logger.debug("serviceId:【", serviceId, "】,serviceStep【", serviceStep, "】,clientIp【", ip, "】");
					}
					paramObjs[i] = response;
				}

			}

		}

		try {
			return (ServiceResult<?>) method.invoke(serviceResource, paramObjs);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getAnnotationsPathList() {
		return annotationsPathList;
	}

}
