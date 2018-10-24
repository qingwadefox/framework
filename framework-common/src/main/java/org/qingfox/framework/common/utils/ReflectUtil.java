package org.qingfox.framework.common.utils;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;

import org.apache.commons.lang3.ArrayUtils;

public class ReflectUtil {

	/**
	 * 设置field的值
	 * 
	 * @param field
	 * @param object
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("static-access")
	public static void setFieldValue(Object object, Field field, Object value) throws IllegalArgumentException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Object converObject = ConvertUtil.convert(value, field.getType());
		if (field.PUBLIC == 0) {
			invokMethod(object, getSMethodStr(field.getName()), false, converObject);
		} else {
			field.set(object, converObject);
		}
	}

	/**
	 * 对比两个方法是否一样
	 * 
	 * @param srcMethod
	 * @param objMethod
	 * @return
	 */
	public static boolean equalsMethod(Method srcMethod, Method objMethod) {
		if (srcMethod.getName().equals(objMethod.getName())) {
			Class<?>[] srcpts = srcMethod.getParameterTypes();
			Class<?>[] objpts = objMethod.getParameterTypes();
			if (srcpts.length == objpts.length) {
				for (int i = 0; i < srcpts.length; i++) {
					if (!srcpts[i].equals(objpts[i])) {
						return false;
					}
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * 设置field的值
	 * 
	 * @param object
	 * @param name
	 * @param value
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws NoSuchFieldException
	 */
	@SuppressWarnings("unchecked")
	public static void setFieldValue(Object object, String name, Object value) throws IllegalArgumentException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {

		if (existInterfaces(object.getClass(), Map.class)) {
			((Map<Object, Object>) object).put(name, value);
		} else {

			Field field = object.getClass().getDeclaredField(name);
			setFieldValue(object, field, value);
		}

	}

	/**
	 * 获取field值
	 * 
	 * @param object
	 * @param name
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public static Object getFieldValue(Object object, String name) throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		if (existInterfaces(object.getClass(), Map.class)) {
			return ((Map<Object, Object>) object).get(name);
		} else {
			Field field = object.getClass().getDeclaredField(name);
			return getFieldValue(object, field);
		}
	}

	/**
	 * 获取field值
	 * 
	 * @param object
	 * @param field
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	@SuppressWarnings("static-access")
	public static Object getFieldValue(Object object, Field field) throws IllegalArgumentException, IllegalAccessException, SecurityException, InvocationTargetException, NoSuchMethodException {
		
		if(object == null || field == null) {
			return null;
		}
		if (field.PUBLIC == 0) {
			return object.getClass().getMethod(getGMethodStr(field.getName())).invoke(object);
		} else {
			return field.get(object);
		}
	}

	/**
	 * 获取field值根据GET方法
	 * 
	 * @param object
	 * @param field
	 * @return
	 */
	public static Object getFieldValueByGetMethod(Object object, Field field) {
		try {
			return object.getClass().getMethod(getGMethodStr(field.getName())).invoke(object);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取field值根据GET方法
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValueByGetMethod(Object object, String fieldName) {
		try {
			return object.getClass().getMethod(getGMethodStr(fieldName)).invoke(object);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 类是否存在annotationClass
	 * 
	 * @param clazz
	 * @param annotationClass
	 * @return
	 */
	public static boolean existAnnotation(Class<?> clazz, Class<?> annotationClass) {
		Annotation[] annotations = clazz.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(annotationClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字段是否存在annotationClass
	 * 
	 * @param field
	 * @param annotationClass
	 * @return
	 */
	public static boolean existAnnotation(Field field, Class<?> annotationClass) {
		Annotation[] annotations = field.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(annotationClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 方法是否存在annotationClass
	 * 
	 * @param method
	 * @param annotationClass
	 * @return
	 */
	public static boolean existAnnotation(Method method, Class<?> annotationClass) {
		Annotation[] annotations = method.getAnnotations();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(annotationClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据FIELDNAME获取get 方法名
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getGMethodStr(String fieldName) {
		return "get" + StrUtil.toUpperCase(fieldName, 0, 1);
	}

	/**
	 * 根据FIELDNAME获取set 方法名
	 * 
	 * @param fieldName
	 * @return
	 */
	public static String getSMethodStr(String fieldName) {
		return "set" + StrUtil.toUpperCase(fieldName, 0, 1);

	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
	 * extends HibernateDao<User>
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> Class<?> getSuperClassGenricType(Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Class<?> getFieldGenricType(Field field, int index) {
		Type genType = field.getGenericType();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
	 * 
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 获取所有的父类
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> getSuperClasses(Class<?> clazz) {
		List<Class<?>> clzzList = new ArrayList<Class<?>>();
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null) {
			clzzList.add(superClazz);
			List<Class<?>> _clzzList = getSuperClasses(superClazz);
			if (!_clzzList.isEmpty()) {
				clzzList.addAll(_clzzList);
			}
		}
		return clzzList;
	}

	/**
	 * 是否存在父类
	 * 
	 * @param clazz
	 * @param existClasses
	 * @return
	 */
	public static boolean existSuperClasses(Class<?> clazz, Class<?> existClasses) {
		if (clazz == null || existClasses == null) {
			return false;
		}
		List<Class<?>> list = getSuperClasses(clazz);
		if (list.indexOf(existClasses) == -1) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 是否存在接口
	 * 
	 * @param clazz
	 * @param existClasses
	 * @return
	 */
	public static boolean existInterfaces(Class<?> clazz, Class<?> existClasses) {
		if (clazz == null || existClasses == null) {
			return false;
		}
		Class<?>[] list = clazz.getInterfaces();
		if (ArrayUtils.indexOf(list, existClasses) == -1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取所有字段
	 * 
	 * @param clazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz) {
		Field[] fields = ArrayUtils.addAll(null, clazz.getDeclaredFields());
		List<Class<?>> superClassList = ReflectUtil.getSuperClasses(clazz);
		for (Class<?> _clazz : superClassList) {
			fields = ArrayUtils.addAll(fields, _clazz.getDeclaredFields());
		}
		return fields;
	}

	/**
	 * 获取所有字段以及父级字段并且根据CLASS类过滤
	 * 
	 * @param clazz
	 * @param containClazz
	 * @return
	 */
	public static Field[] getFields(Class<?> clazz, Class<?>... containClazz) {
		Field[] fields = ArrayUtils.addAll(null, clazz.getDeclaredFields());
		List<Class<?>> superClassList = ReflectUtil.getSuperClasses(clazz);
		for (Class<?> _clazz : superClassList) {
			for (Class<?> _clazz_ : containClazz) {
				if (_clazz == _clazz_) {
					fields = ArrayUtils.addAll(fields, _clazz.getDeclaredFields());
				}
			}
		}
		return fields;
	}

	/**
	 * 根据类、方法名获取所有方法
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz, String name) {
		List<Method> list = new ArrayList<Method>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				list.add(method);
			}
		}
		methods = new Method[list.size()];
		return list.toArray(methods);
	}

	/**
	 * 根据类、方法名、注解获取所有方法
	 * 
	 * @param clazz
	 * @param name
	 * @param annotationClazz
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz, String name, Class<?> annotationClazz) {
		List<Method> list = new ArrayList<Method>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (method.getName().equals(name)) {
				if (existAnnotation(method, annotationClazz)) {
					list.add(method);
				}
			}
		}
		methods = new Method[list.size()];
		return list.toArray(methods);
	}

	/**
	 * 根据类、注解获取所有方法
	 * 
	 * @param clazz
	 * @param annotationClazz
	 * @return
	 */
	public static Method[] getMethods(Class<?> clazz, Class<?> annotationClazz) {

		List<Method> list = new ArrayList<Method>();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if (existAnnotation(method, annotationClazz)) {
				list.add(method);
			}
		}
		methods = new Method[list.size()];
		return list.toArray(methods);
	}

	/**
	 * 执行某个method
	 * 
	 * @param classObj
	 * @param name
	 * @param isMatch
	 * @param parameters
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object invokMethod(Object classObj, String name, Boolean isMatch, Object... parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		Method[] methods = getMethods(classObj.getClass(), name);
		Method method = null;
		boolean match = false;
		Class<?>[] parameterTypes = null;
		for (Method _method : methods) {
			parameterTypes = _method.getParameterTypes();
			if (parameterTypes.length == parameters.length) {
				method = _method;
				if (!isMatch) {
					break;
				}
				boolean _match = true;
				int i = 0;
				for (Object parameter : parameters) {
					if (!parameter.getClass().equals(parameterTypes[i])) {
						_match = false;
						break;
					}
					i++;
				}

				if (_match) {
					match = true;
					break;
				}
			}
		}
		if (method == null) {
			return null;
		}

		// 转换匹配参数
		Object[] _paramters = null;
		if (!match && isMatch && parameterTypes != null && parameterTypes.length != 0) {
			_paramters = new Object[parameters.length];
			for (int i = 0; i < parameters.length; i++) {
				_paramters[i] = ConvertUtil.convert(parameters[i], parameterTypes[i]);
			}
		}

		if (_paramters == null) {
			_paramters = parameters;
		}

		return method.invoke(classObj, _paramters);
	}

	/**
	 * 执行某个method
	 * 
	 * @param classObj
	 * @param name
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object invokMethod(Object classObj, String name) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return invokMethod(classObj, name, true, new Object[0]);
	}

	/**
	 * 执行某个method 不需要匹配
	 * 
	 * @param classObj
	 * @param name
	 * @param parameters
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object invokMethodUnmatch(Object classObj, String name, Object... parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return invokMethod(classObj, name, false, parameters);
	}

	/**
	 * 执行某个method 需要匹配
	 * 
	 * @param classObj
	 * @param name
	 * @param parameters
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object invokMethodmatch(Object classObj, String name, Object... parameters) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return invokMethod(classObj, name, false, parameters);
	}

	public static void main(String[] args) {
		System.out.println(existSuperClasses(JButton.class, Component.class));
	}
}
