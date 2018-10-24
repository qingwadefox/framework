package com.framework.plugin.web.common.web.spring.factory;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.web.spring.utils.SpringUtil;

@Component
public class CacheFactory implements InitializingBean {

    @Autowired
    private SpringFactory springFactory;

    @Autowired
    private DataBaseFactory dataBaseFactory;

    @SuppressWarnings("rawtypes")
    public void afterPropertiesSet() throws Exception {
        Map<String, CacheInf> chacheMap = SpringUtil.getCaches();

        for (CacheInf cache : chacheMap.values()) {
            cache.create();
        }
    }
}
