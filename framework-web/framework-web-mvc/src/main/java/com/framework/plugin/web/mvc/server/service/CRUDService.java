package com.framework.plugin.web.common.server.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.plugin.web.common.server.dao.CRUDDao;
import com.framework.plugin.web.common.server.request.CRUDRequest;

@Service
public class CRUDService {

    @Autowired
    private CRUDDao dao;

    /**
     * 查询
     * 
     * @param request
     * @return 记录集
     */
    public List<Map<String, String>> search(CRUDRequest request) {
        return dao.select(request);
    }

    /**
     * 新增
     * 
     * @param request
     * @return 记录集
     */
    public List<Map<String, String>> add(CRUDRequest request) {
        return dao.insert(request);
    }

    /**
     * 删除
     * 
     * @param request
     * @return 记录集
     */
    public List<Map<String, String>> del(CRUDRequest request) {
        return dao.delete(request);
    }

    /**
     * 修改
     * 
     * @param request
     * @return 记录集
     */
    public List<Map<String, String>> edit(CRUDRequest request) {
        return dao.update(request);
    }

}
