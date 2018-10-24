package com.framework.plugin.web.common.server.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.framework.common.utils.DateUtil;

public class DataUtil {
    public static void dataEscape(LinkedHashMap<String, String> data) {
        Iterator<String> it = data.keySet().iterator();
        List<String> removeKey = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            if (data.get(key) == null) {
                continue;
            }
            // nowdate
            if (data.get(key).equals("{NOWDATE}")) {
                data.put(key, DateUtil.getNowDate());
            }
            // null
            else if (data.get(key).equals("{NULL}")) {
                removeKey.add(key);
            }
        }

        for (String key : removeKey) {
            data.remove(key);
        }

    }
}
