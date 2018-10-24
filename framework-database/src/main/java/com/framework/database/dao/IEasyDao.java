package com.framework.database.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.framework.database.beans.ValueBean;
import com.framework.database.entity.IEntity;

public interface IEasyDao {

	public void changeDataBase(String dbName);

	public Map<String, ValueBean> select(String table, Map<String, Object> param, String... field);

	public <T extends IEntity<?>> T selectEntity(Class<T> entityClass, Map<String, Object> param) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public List<Map<String, ValueBean>> selectList(String table, Map<String, Object> param, String... field);

	public <T extends IEntity<?>> List<T> selectEntityList(Class<T> entityClass, Map<String, Object> param) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public ValueBean selectValue(String table, Map<String, Object> param, String field);

	public List<ValueBean> selectValueList(String table, Map<String, Object> param, String field);

	public Map<String, ValueBean> select(String table);

	public Map<String, ValueBean> select(String table, String... field);

	public Map<String, ValueBean> select(String table, Map<String, Object> param);

	public List<Map<String, ValueBean>> selectList(String table);

	public List<Map<String, ValueBean>> selectList(String table, String... field);

	public List<Map<String, ValueBean>> selectList(String table, Map<String, Object> param);

	public <T extends IEntity<?>> T selectEntity(Class<T> entityClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> List<T> selectEntityList(Class<T> entityClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public ValueBean selecValue(String table, String field);

	public List<ValueBean> selecValueList(String table, String field);

	public <T extends IEntity<?>> void updateKey(T entity);

	public <T extends IEntity<?>> void updateKey(T entity, Object key);

	public <T extends IEntity<?>> void insert(T entity);

	public <T extends IEntity<?>> void insert(List<T> entitys);

	public <T extends IEntity<?>> void deleteKey(T entity);

	public <T extends IEntity<?>> void deleteKey(Class<T> entityClass, Object key);

	public void update(String table, Map<String, Object> data, Map<String, Object> param);

	public void insert(String table, Map<String, Object> data);

	public void insert(String table, List<Map<String, Object>> datas);

	public void delete(String table, Map<String, Object> param);

	public <T extends IEntity<?>> List<T> selectSqlEntityList(Class<T> entityClass, String sql) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> List<T> selectSqlEntityList(Class<T> entityClass, String sql, Map<String, Object> param) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> T selectSqlEntity(Class<T> entityClass, String sql) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> T selectSqlEntity(Class<T> entityClass, String sql, Map<String, Object> param) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> T selectKey(T entity) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

	public <T extends IEntity<?>> T selectKey(Class<T> entityClass, Serializable key) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException;

}
