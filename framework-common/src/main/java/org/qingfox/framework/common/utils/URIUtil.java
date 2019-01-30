package org.qingfox.framework.common.utils;

import org.apache.commons.lang3.StringUtils;

public class URIUtil {

    public static final String URL_SPLIT = "/";

    /**
     * 获取url参数
     * 
     * @param url
     * @param name
     * @return
     */
    public static String getUrlParam(String url, String name) {
        String[] paramStrs = url.split("\\?");
        if (paramStrs.length == 1) {
            return null;
        }

        String paramStr = paramStrs[1];
        for (String _paramStr : paramStr.split("&")) {

            if (_paramStr.indexOf("=") == -1) {
                continue;
            } else {
                String[] _paramStrs = _paramStr.split("=");
                if (_paramStrs[0].equals(name)) {
                    return _paramStrs[1];
                }
            }

        }
        return null;
    }

    public static String appendUrlPath(String url, String path) {
        if (!url.endsWith(URL_SPLIT)) {
            url += URL_SPLIT;
        }
        return url + (StringUtils.isEmpty(path) ? "" : path + URL_SPLIT);
    }

    public static String appendUrlResource(String url, String resource) {
        if (!url.endsWith(URL_SPLIT)) {
            url += URL_SPLIT;
        }
        return url + resource.replace(URL_SPLIT, "");

    }
}
