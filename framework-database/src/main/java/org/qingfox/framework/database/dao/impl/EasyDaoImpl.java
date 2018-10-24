package org.qingfox.framework.database.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.qingfox.framework.database.beans.ConditionBean;
import org.qingfox.framework.database.beans.PageBean;
import org.qingfox.framework.database.beans.ValueBean;
import org.qingfox.framework.database.dao.IEasyDao;
import org.qingfox.framework.database.entity.IEntity;
import org.qingfox.framework.database.enums.DBTypeEnum;
import org.qingfox.framework.database.factory.DataBaseFactory;
import org.qingfox.framework.database.utils.EntityUtil;
import org.qingfox.framework.database.utils.SqlUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

public class EasyDaoImpl implements IEasyDao {

	private JdbcTemplate jdbcTemplate;
	private DBTypeEnum dbType;
	private NamedParameterJdbcTemplate npJdbcTemplate;

	public EasyDaoImpl() {
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
		npJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public void changeDataBase(String dbName) {
		DataBaseFactory.changeDataBase(dbName);
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
		npJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	public Map<String, ValueBean> select(String table) {
		return this.select(table, null, new String[0]);
	}

	public Map<String, ValueBean> select(String table, String... field) {
		return this.select(table, null, field);
	}

	public Map<String, ValueBean> select(String table, Map<String, Object> param) {
		return this.select(table, param, new String[0]);
	}

	public List<Map<String, ValueBean>> selectList(String table) {
		return this.selectList(table, null, new String[0]);
	}

	public List<Map<String, ValueBean>> selectList(String table,
			String... field) {
		return this.selectList(table, null, field);
	}

	public List<Map<String, ValueBean>> selectList(String table,
			Map<String, Object> param) {
		return this.selectList(table, param, new String[0]);
	}

	public <T extends IEntity<?>> T selectEntity(Class<T> entityClass)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		return this.selectEntity(entityClass, null);
	}

	public <T extends IEntity<?>> List<T> selectEntityList(Class<T> entityClass)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		return this.selectEntityList(entityClass, null);
	}

	public ValueBean selecValue(String table, String field) {
		return this.selectValue(table, null, field);
	}

	public List<ValueBean> selecValueList(String table, String field) {
		return this.selectValueList(table, null, field);
	}

	public <T extends IEntity<?>> void insert(List<T> entitys) {
		for (T entity : entitys) {
			this.insert(entity);
		}
	}

	public void insert(String table, List<Map<String, Object>> datas) {
		for (Map<String, Object> data : datas) {
			this.insert(table, data);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, ValueBean> select(String table,
			Map<String, Object> param, String... field) {
		List<ConditionBean> conditions = SqlUtil.paramToCondition(param);
		// param
		Object[] paramArray = SqlUtil.getSelectParams(conditions);
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, table,
				CollectionUtils.arrayToList(field), conditions);
		PageBean<Map<String, ValueBean>> page = new PageBean<Map<String, ValueBean>>();
		page.setPageIndex(1);
		page.setRecordNumber(1);
		String pageSql = SqlUtil.getPageSql(dbType, sql, page);
		List<Map<String, ValueBean>> list = jdbcTemplate.query(pageSql,
				new RowMapper<Map<String, ValueBean>>() {
					public Map<String, ValueBean> mapRow(ResultSet arg0,
							int arg1) throws SQLException {
						return SqlUtil.getResult(arg0);
					}
				}, paramArray);
		if ((list != null && !list.isEmpty())) {
			return list.get(0);
		} else
			return null;
	}

	public <T extends IEntity<?>> T selectEntity(Class<T> entityClass,
			Map<String, Object> param) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		String table = EntityUtil.getTable(entityClass);
		String[] columns = EntityUtil.getColumns(entityClass);
		Map<String, ValueBean> result = select(table, param, columns);

		return EntityUtil.valueMapToEntity(entityClass, result);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, ValueBean>> selectList(String table,
			Map<String, Object> param, String... field) {

		List<ConditionBean> conditions = SqlUtil.paramToCondition(param);

		// param
		Object[] paramArray = SqlUtil.getSelectParams(conditions);
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, table,
				CollectionUtils.arrayToList(field), conditions);
		return jdbcTemplate.query(sql, new RowMapper<Map<String, ValueBean>>() {
			public Map<String, ValueBean> mapRow(ResultSet arg0, int arg1)
					throws SQLException {
				return SqlUtil.getResult(arg0);
			}
		}, paramArray);
	}

	public <T extends IEntity<?>> List<T> selectEntityList(
			Class<T> entityClass, Map<String, Object> param)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		String table = EntityUtil.getTable(entityClass);
		String[] columns = EntityUtil.getColumns(entityClass);
		List<Map<String, ValueBean>> resultList = selectList(table, param,
				columns);
		List<T> list = new ArrayList<T>();

		for (Map<String, ValueBean> result : resultList) {
			list.add(EntityUtil.valueMapToEntity(entityClass, result));
		}
		return list;
	}

	public ValueBean selectValue(String table, Map<String, Object> param,
			String field) {
		Map<String, ValueBean> result = this.select(table, param, field);
		if (result != null && result.size() > 0) {
			return result.get(field);
		}
		return null;
	}

	public List<ValueBean> selectValueList(String table,
			Map<String, Object> param, String field) {
		List<Map<String, ValueBean>> resultList = this.selectList(table, param,
				field);
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		List<ValueBean> valueList = new ArrayList<ValueBean>();
		for (Map<String, ValueBean> resultMap : resultList) {
			valueList.add(resultMap.get(field));
		}
		return valueList;
	}

	public <T extends IEntity<?>> void insert(T entity) {
		Map<String, Object> data = entity.toMap();
		String table = entity.getTable();
		this.insert(table, data);
	}

	public void insert(String table, Map<String, Object> data) {
		List<Object> insertParam = new ArrayList<Object>();
		List<String> insertFields = new ArrayList<String>();
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object value = data.get(key);
			if (value == null) {
				continue;
			}
			insertFields.add(key);
			insertParam.add(data.get(key));
		}
		String sql = SqlUtil.getInsertSql(dbType, table, insertFields);
		jdbcTemplate.update(sql, insertParam.toArray());

	}

	public void update(String table, Map<String, Object> data,
			Map<String, Object> param) {

		List<ConditionBean> conditions = SqlUtil.paramToCondition(param);

		List<String> updateFields = new ArrayList<String>();
		List<Object> updateParams = new ArrayList<Object>();
		Iterator<Entry<String, Object>> dataIt = data.entrySet().iterator();
		while (dataIt.hasNext()) {
			Entry<String, Object> entry = dataIt.next();
			updateFields.add(entry.getKey());
			updateParams.add(entry.getValue());
		}
		// param
		Object[] selectParam = SqlUtil.getSelectParams(conditions);
		Object[] params = ArrayUtils
				.addAll(updateParams.toArray(), selectParam);

		// sql
		String sql = SqlUtil.getUpdateSql(dbType, table, updateFields,
				conditions);
		jdbcTemplate.update(sql.toString(), params);
	}

	public <T extends IEntity<?>> void updateKey(T entity) {
		String table = entity.getTable();
		Map<String, Object> data = entity.toMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(entity.getKey(), entity.getId());
		this.update(table, data, param);
	}

	public <T extends IEntity<?>> void updateKey(T entity, Object key) {
		String table = entity.getTable();
		Map<String, Object> data = entity.toMap();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(entity.getKey(), key);
		this.update(table, data, param);
	}

	public <T extends IEntity<?>> void deleteKey(T entity) {
		String table = entity.getTable();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(entity.getKey(), entity.getId());
		this.delete(table, param);

	}

	public <T extends IEntity<?>> void deleteKey(Class<T> entityClass,
			Object key) {
		String table = EntityUtil.getTable(entityClass);
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(EntityUtil.getIDName(entityClass), key);
		this.delete(table, param);

	}

	public void delete(String table, Map<String, Object> param) {
		List<ConditionBean> conditions = SqlUtil.paramToCondition(param);
		// param
		Object[] _param = SqlUtil.getSelectParams(conditions);
		// sql
		String sql = SqlUtil.getDeleteSql(dbType, table, conditions);
		jdbcTemplate.update(sql.toString(), _param);

	}

	public <T extends IEntity<?>> List<T> selectSqlEntityList(
			Class<T> entityClass, String sql, Map<String, Object> param)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		List<Map<String, Object>> resultList = npJdbcTemplate.queryForList(sql,
				param);
		return EntityUtil.mapToEntityList(entityClass, resultList);
	}

	public <T extends IEntity<?>> List<T> selectSqlEntityList(
			Class<T> entityClass, String sql) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		return this.selectSqlEntityList(entityClass, sql,
				new HashMap<String, Object>());
	}

	public <T extends IEntity<?>> T selectSqlEntity(Class<T> entityClass,
			String sql) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		return this.selectSqlEntity(entityClass, sql,
				new HashMap<String, Object>());
	}

	public <T extends IEntity<?>> T selectSqlEntity(Class<T> entityClass,
			String sql, Map<String, Object> param)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Map<String, Object> map = npJdbcTemplate.queryForMap(sql, param);
		return EntityUtil.mapToEntity(entityClass, map);
	}

	@SuppressWarnings("unchecked")
	public <T extends IEntity<?>> T selectKey(T entity)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(entity.getKey(), entity.getId());
		return (T) selectEntity(entity.getClass(), param);
	}

	public <T extends IEntity<?>> T selectKey(Class<T> entityClass,
			Serializable key) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		T entity = entityClass.newInstance();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(entity.getKey(), key);
		return this.selectEntity(entityClass, param);
	}

}
