package org.qingfox.framework.database.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qingfox.framework.database.beans.ValueBean;
import org.qingfox.framework.database.entity.IEntity;
import org.qingfox.framework.database.stereotypes.Column;
import org.qingfox.framework.database.stereotypes.Table;

import com.framework.common.utils.ReflectUtil;

public class EntityUtil {

	public static String getTable(Class<?> entityClass) {
		return entityClass.getAnnotation(Table.class).value();
	}

	public static String[] getColumns(Class<?> entityClass) {
		List<String> columnList = new ArrayList<String>();
		Field[] fields = ReflectUtil.getFields(entityClass);
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && !column.virtual()) {
				columnList.add(column.value());
			}
		}
		return columnList.toArray(new String[columnList.size()]);
	}

	public static String getIDName(Class<?> entityClass) {
		Field[] fields = ReflectUtil.getFields(entityClass);
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && column.id() == true) {
				return column.value();
			}
		}
		return null;
	}

	public static <T extends IEntity<?>> List<T> mapToEntityList(
			Class<T> entityClass, List<Map<String, Object>> entityMapList)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		List<T> list = new ArrayList<T>();
		for (Map<String, Object> map : entityMapList) {
			list.add(mapToEntity(entityClass, map));
		}
		return list;
	}

	public static <T extends IEntity<?>> T mapToEntity(Class<T> entityClass,
			Map<String, Object> entityMap) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {

		if (entityMap == null || entityClass == null) {
			return null;
		}

		if (entityClass.getAnnotation(Table.class) == null) {
			return null;
		}

		Map<String, Field> fieldMap = new HashMap<String, Field>();
		for (Field field : entityClass.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				fieldMap.put(column.value().toUpperCase(), field);
			}
		}
		T entity = entityClass.newInstance();

		Iterator<Entry<String, Field>> it = fieldMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Field> entry = it.next();
			Object value = entityMap.get(entry.getKey());
			if (value != null && value != null) {
				ReflectUtil.setFieldValue(entity, entry.getValue(), value);
			}
		}
		return entity;
	}

	public static <T extends IEntity<?>> T valueMapToEntity(
			Class<T> entityClass, Map<String, ValueBean> entityMap)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {

		if (entityMap == null || entityClass == null) {
			return null;
		}

		if (entityClass.getAnnotation(Table.class) == null) {
			return null;
		}

		Map<String, Field> fieldMap = new HashMap<String, Field>();
		for (Field field : entityClass.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				fieldMap.put(column.value().toUpperCase(), field);
			}
		}
		T entity = entityClass.newInstance();

		Iterator<Entry<String, Field>> it = fieldMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Field> entry = it.next();
			ValueBean value = entityMap.get(entry.getKey());
			if (value != null && value.get() != null) {
				ReflectUtil
						.setFieldValue(entity, entry.getValue(), value.get());
			}
		}

		return entity;

	}

	public static Map<String, Object> entityToMap(IEntity<?> entity) {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fields = ReflectUtil.getFields(entity.getClass());
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				try {
					map.put(column.value(),
							ReflectUtil.getFieldValue(entity, field));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return map;
	}
}
