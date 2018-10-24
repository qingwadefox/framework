package com.framework.plugin.web.common.server.consoller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.plugin.web.common.server.consoller.basic.BasicController;
import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.filter.basic.BasicAppFilter;
import com.framework.plugin.web.common.server.request.ServiceRequest;
import com.framework.plugin.web.common.server.response.ServiceResponse;
import com.framework.plugin.web.common.stereotype.AppFilter;
import com.framework.plugin.web.common.web.spring.factory.SpringFactory;

@RestController
@RequestMapping("/frame/app")
public class AppConsoller extends BasicController {

    @Autowired
    private SpringFactory springFactory;

    @RequestMapping("/doApp")
    public void doApp(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            @RequestBody ServiceRequest request) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException,
            ClassNotFoundException {
        String appCode = request.getAppCode();
        String appFunction = request.getAppFunction();

        if (StringUtils.isNotEmpty(appCode)
                && StringUtils.isNotEmpty(appFunction)) {
            ServiceResponse<Object> sResponse = this
                    .getServiceResponse(request);

            List<BasicAppFilter> list = springFactory.getAppFilterList();
            boolean pass = true;

            for (BasicAppFilter appFilter : list) {
                AppFilter appFilterDt = appFilter.getClass().getAnnotation(
                        AppFilter.class);
                String[] includeApps = appFilterDt.includeApps();
                String[] excludeApps = appFilterDt.excludeApps();

                if (ArrayUtils.indexOf(excludeApps, appCode) != -1) {
                    continue;
                }

                if (includeApps.length == 0
                        || (includeApps.length > 0 && ArrayUtils.indexOf(
                                includeApps, appCode) != -1)) {
                    if (appFilter.execute(hResponse, hRequest, hSession,
                            sResponse, request, appCode, appFunction).equals(
                            BasicAppFilter.NOT_PASS)) {
                        pass = false;
                        break;
                    }
                }

            }
            Object result = null;
            if (pass) {
                Map<String, Object> parameter = request.getParameter();
                result = springFactory.invokeMethodApp(hResponse, hRequest,
                        hSession, appCode, appFunction, parameter);

            } else {
                throw new ServiceException(ExceptionEnum.FILTERUNKNOW);
            }

            sResponse.setResult(result);
            sResponse.print(hResponse);
        } else {
            throw new ServiceException(ExceptionEnum.APPWRONGPARAM);
        }

    }
}
