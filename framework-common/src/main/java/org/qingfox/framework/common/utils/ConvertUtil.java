package org.qingfox.framework.common.utils;

import org.apache.commons.beanutils.ConvertUtils;
import org.qingfox.framework.common.enums.ClassEnum;

public class ConvertUtil {

	public static <T> T convert(Object value, Class<T> targetType) {
		return convert(value, targetType, null);

	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Object value, Class<T> targetType, Object param) {
		switch (ClassEnum.getEnmu(value.getClass().getName())) {
		case ORLTIMESTAMP:
			try {
				return convert(ReflectUtil.invokMethod(value, "timestampValue"), targetType, param);
			} catch (Exception e) {
				return null;
			}
		default:
			return (T) ConvertUtils.convert(value, targetType);
		}
	}
}
