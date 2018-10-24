package com.framework.plugin.web.system.caches;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.service.CRUDService;
import com.framework.plugin.web.common.stereotype.Cachecode;
import com.framework.plugin.web.system.bean.DictBean;

@Cachecode(code = "dictcache")
public class DictCache implements CacheInf<DictBean> {

    @Autowired
    private CRUDService CRUDService;

    private Map<String, DictBean> datas;

    @Override
    public void create() {
        datas = new HashMap<String, DictBean>();
        CRUDRequest request = new CRUDRequest();
        request.setTable("T_SYSTEM_DICT");
        List<Map<String, String>> result = CRUDService.search(request);
        for (Map<String, String> map : result) {

            String code = map.get("CODE");
            String id = map.get("ID");
            DictBean dictBean = datas.get(code);
            if (dictBean == null) {
                dictBean = new DictBean();
                dictBean.setCode(map.get("CODE"));
                dictBean.setText(map.get("TEXT"));
                dictBean.setValue(map.get("VALUE"));

                List<DictBean> list = new ArrayList<DictBean>();
                for (Map<String, String> _map : result) {
                    DictBean _dictBean = new DictBean();
                    _dictBean.setCode(_map.get("CODE"));
                    _dictBean.setText(_map.get("TEXT"));
                    _dictBean.setValue(_map.get("VALUE"));
                    if (_map.get("PID").equals(id)) {
                        list.add(_dictBean);
                    }
                }
                dictBean.setList(list);
                datas.put(code, dictBean);
            }
        }

    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void refresh() {
        this.create();

    }

    @Override
    public DictBean get(String key) {
        return datas.get(key);
    }

    @Override
    public void set(String key, DictBean obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public void remove(String key) {
        // TODO Auto-generated method stub
        
    }

}
