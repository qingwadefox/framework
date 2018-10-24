package com.framework.common.utils;

public class URIUtil {
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
}
