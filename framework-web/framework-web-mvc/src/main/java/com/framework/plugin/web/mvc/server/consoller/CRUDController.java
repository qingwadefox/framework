package com.framework.plugin.web.common.server.consoller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framework.plugin.web.common.server.consoller.basic.BasicController;
import com.framework.plugin.web.common.server.enums.ExceptionEnum;
import com.framework.plugin.web.common.server.exception.ServiceException;
import com.framework.plugin.web.common.server.filter.basic.BasicAppFilter;
import com.framework.plugin.web.common.server.filter.basic.BasicCRUDFilter;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.response.CRUDResponse;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.web.spring.factory.SpringFactory;

@RestController
@RequestMapping("/frame/crud")
public class CRUDController extends BasicController {

    @Autowired
    private SpringFactory springFactory;

    @Autowired
    private CRUDService service;

    @RequestMapping("/search")
    public void search(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            @RequestBody CRUDRequest cRequest) {
        CRUDResponse cResponse = this.getCRUDResponse(cRequest);
        cResponse.setPage(cRequest.getPage());

        // filter
        List<BasicCRUDFilter> list = springFactory.getCRUDFilterList();
        boolean pass = true;
        for (BasicCRUDFilter crudFilter : list) {
            if (crudFilter.execute(hResponse, hRequest, hSession, cResponse,
                    cRequest, BasicCRUDFilter.OP_SEARCH).equals(
                    BasicAppFilter.NOT_PASS)) {
                pass = false;
                break;
            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.FILTERUNKNOW);
        }

        cResponse.setDatas(service.search(cRequest)).print(hResponse);
    }

    @RequestMapping(value = "/add")
    public void add(HttpServletResponse hResponse, HttpServletRequest hRequest,
            HttpSession hSession, @RequestBody CRUDRequest cRequest) {
        CRUDResponse cResponse = this.getCRUDResponse(cRequest);

        // filter
        List<BasicCRUDFilter> list = springFactory.getCRUDFilterList();
        boolean pass = true;
        for (BasicCRUDFilter crudFilter : list) {
            if (crudFilter.execute(hResponse, hRequest, hSession, cResponse,
                    cRequest, BasicCRUDFilter.OP_ADD).equals(
                    BasicAppFilter.NOT_PASS)) {
                pass = false;
                break;
            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.FILTERUNKNOW);
        }

        cResponse.setDatas(service.add(cRequest)).print(hResponse);

    }

    @RequestMapping("/del")
    public void del(HttpServletResponse hResponse, HttpServletRequest hRequest,
            HttpSession hSession, @RequestBody CRUDRequest cRequest) {
        CRUDResponse cResponse = this.getCRUDResponse(cRequest);

        // filter
        List<BasicCRUDFilter> list = springFactory.getCRUDFilterList();
        boolean pass = true;
        for (BasicCRUDFilter crudFilter : list) {
            if (crudFilter.execute(hResponse, hRequest, hSession, cResponse,
                    cRequest, BasicCRUDFilter.OP_DEL).equals(
                    BasicAppFilter.NOT_PASS)) {
                pass = false;
                break;
            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.FILTERUNKNOW);
        }
        cResponse.setDatas(service.del(cRequest)).print(hResponse);
    }

    @RequestMapping("/edit")
    public void edit(HttpServletResponse hResponse,
            HttpServletRequest hRequest, HttpSession hSession,
            @RequestBody CRUDRequest cRequest) {

        CRUDResponse cResponse = this.getCRUDResponse(cRequest);

        // filter
        List<BasicCRUDFilter> list = springFactory.getCRUDFilterList();
        boolean pass = true;
        for (BasicCRUDFilter crudFilter : list) {
            if (crudFilter.execute(hResponse, hRequest, hSession, cResponse,
                    cRequest, BasicCRUDFilter.OP_EDIT).equals(
                    BasicAppFilter.NOT_PASS)) {
                pass = false;
                break;
            }
        }

        if (!pass) {
            throw new ServiceException(ExceptionEnum.FILTERUNKNOW);
        }

        cResponse.setDatas(service.edit(cRequest)).print(hResponse);
    }

}
