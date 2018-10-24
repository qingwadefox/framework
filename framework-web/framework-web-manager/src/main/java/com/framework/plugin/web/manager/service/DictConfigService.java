package com.framework.plugin.web.system.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.plugin.web.common.stereotype.Appcode;
import com.framework.plugin.web.common.stereotype.Appfunction;
import com.framework.plugin.web.system.bean.DictBean;
import com.framework.plugin.web.system.caches.DictCache;

@Service
@Appcode(code = "DictConfigService")
public class DictConfigService {

    @Autowired
    private DictCache dictCache;

    @Appfunction(code = "getDict", paramNames = { "code" })
    public DictBean getDict(String code) throws IOException {
        return dictCache.get(code);
    }

    @Appfunction(code = "getDictMap", paramNames = { "codes" })
    public Map<String, DictBean> getDictMap(String codes) {
        String[] code = codes.split(",");
        Map<String, DictBean> result = new HashMap<String, DictBean>();
        for (String _code : code) {
            result.put(_code, dictCache.get(_code));
        }
        return result;
    }

    @Appfunction(code = "refreshCache")
    public void refreshCache() {
        dictCache.refresh();
    }
}
