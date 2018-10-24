package com.framework.plugin.web.common.web.spring.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.framework.common.infs.CacheInf;
import com.framework.plugin.web.common.server.sequences.inf.SequenceInf;
import com.framework.plugin.web.common.server.sequences.stereotype.Sequence;
import com.framework.plugin.web.common.stereotype.Cachecode;

public class SpringUtil {

    public static ApplicationContext applicationContext;
    public static ServletContext servletContext;

    public static Object getBean(Class<?> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String beanName) {
        if (applicationContext.containsBean(beanName)) {
            return applicationContext.getBean(beanName);
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Map<String, CacheInf> getCaches() {
        Map<String, Object> map = applicationContext
                .getBeansWithAnnotation(Cachecode.class);
        Map<String, CacheInf> cacheMap = new HashMap<String, CacheInf>();

        Iterator<Entry<String, Object>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            CacheInf cache = (CacheInf) entry.getValue();
            cacheMap.put(
                    cache.getClass().getAnnotation(Cachecode.class).code(),
                    cache);
        }
        return cacheMap;
    }

    @SuppressWarnings("rawtypes")
    public static CacheInf getCache(String key) {
        Map<String, Object> map = applicationContext
                .getBeansWithAnnotation(Cachecode.class);

        Iterator<Entry<String, Object>> it = map.entrySet().iterator();

        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            CacheInf cache = (CacheInf) entry.getValue();
            if (cache.getClass().getAnnotation(Cachecode.class).code()
                    .equals(key)) {
                return cache;
            }
        }
        return null;
    }

    public static Map<String, SequenceInf> getSequence(String table) {
        Map<String, Object> map = applicationContext
                .getBeansWithAnnotation(Sequence.class);
        Map<String, SequenceInf> seqMap = new HashMap<String, SequenceInf>();
        Iterator<Entry<String, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Object> entry = it.next();
            SequenceInf sequence = (SequenceInf) entry.getValue();
            if (sequence.getClass().getAnnotation(Sequence.class).table()
                    .equals(table)) {
                seqMap.put(sequence.getClass().getAnnotation(Sequence.class)
                        .field(), sequence);
            }
        }
        return seqMap;
    }

    public static void setBean(String beanName, Object boject) {
        ConfigurableApplicationContext cApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) cApplicationContext
                .getBeanFactory();

        if (!beanFactory.containsBean(beanName)) {
            beanFactory.registerSingleton(beanName, boject);
        }
    }

}
