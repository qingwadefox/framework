package com.framework.plugin.web.common.server.response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.framework.plugin.web.common.bean.PageBean;

public class TableResponse implements Serializable {
    private static final long serialVersionUID = -4348781749613880002L;

    private List<Map<String, String>> datas;

    private PageBean page;

    public List<Map<String, String>> getDatas() {
        return datas;
    }

    public void setDatas(List<Map<String, String>> datas) {
        this.datas = datas;
    }

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

}
