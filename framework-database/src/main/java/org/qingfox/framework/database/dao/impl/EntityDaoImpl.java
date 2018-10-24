package org.qingfox.framework.database.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.qingfox.framework.database.beans.ConditionBean;
import org.qingfox.framework.database.beans.PageBean;
import org.qingfox.framework.database.beans.ValueBean;
import org.qingfox.framework.database.dao.IEntityDao;
import org.qingfox.framework.database.entity.IEntity;
import org.qingfox.framework.database.entity.base.BaseEntity;
import org.qingfox.framework.database.enums.DBTypeEnum;
import org.qingfox.framework.database.factory.DataBaseFactory;
import org.qingfox.framework.database.request.EntityRequest;
import org.qingfox.framework.database.utils.SqlUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class EntityDaoImpl implements IEntityDao {

	private JdbcTemplate jdbcTemplate;
	private DBTypeEnum dbType;

	public EntityDaoImpl() {
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
	}

	public void changeDataBase(String dbName) {
		DataBaseFactory.changeDataBase(dbName);
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
	}

	public <T extends IEntity<?>> T select(EntityRequest<T> request) {
		DBTypeEnum dbType = DataBaseFactory.getDBType();
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
		PageBean<Map<String, ValueBean>> page = new PageBean<Map<String, ValueBean>>();
		page.setPageIndex(1);
		page.setRecordNumber(1);
		String pageSql = SqlUtil.getPageSql(dbType, sql, page);
		final Class<T> entityClass = request.getEntityClass();
		final Map<String, String> keyMapEntity = request.getKeyMapEntity();
		List<T> list = jdbcTemplate.query(pageSql, new RowMapper<T>() {
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				try {
					return SqlUtil.getResult(arg0, entityClass, keyMapEntity);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, param);
		if ((list != null && !list.isEmpty())) {
			return list.get(0);
		} else
			return null;
	}

	public <T extends IEntity<?>> List<T> selectList(EntityRequest<T> request) {
		DBTypeEnum dbType = DataBaseFactory.getDBType();
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
		final Class<T> entityClass = request.getEntityClass();
		final Map<String, String> keyMapEntity = request.getKeyMapEntity();
		return jdbcTemplate.query(sql, new RowMapper<T>() {
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				try {
					return SqlUtil.getResult(arg0, entityClass, keyMapEntity);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, param);

	}

	public <T extends IEntity<?>> PageBean<T> selectPage(
			EntityRequest<T> request) {

		PageBean<T> page = new PageBean<T>();
		page.setPageIndex(request.getPage().getPageIndex());
		page.setRecordNumber(request.getPage().getRecordNumber());
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
		String pageSql = SqlUtil.getPageSql(dbType, sql, request.getPage());
		String countSql = SqlUtil.getCountSql(sql);
		final Class<T> entityClass = request.getEntityClass();
		final Map<String, String> keyMapEntity = request.getKeyMapEntity();
		List<T> records = jdbcTemplate.query(pageSql, new RowMapper<T>() {
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				try {
					return SqlUtil.getResult(arg0, entityClass, keyMapEntity);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}, param);
		page.setRecords(records);
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(countSql, param);
		if (rowSet.next()) {
			page.setRecordCount(rowSet.getLong(1));
			Long pageCount = page.getRecordCount() / page.getRecordNumber();
			if (page.getRecordCount() < page.getRecordNumber()
					|| page.getRecordCount() % page.getRecordNumber() != 0) {
				pageCount += 1;
			}
			page.setPageCount(pageCount);
		}
		return page;
	}

	public <T extends IEntity<?>> void update(EntityRequest<T> request) {
		if (request.getDatas() == null || request.getDatas().isEmpty()) {
			// TODO throw exception
			return;
		}

		Map<String, Object> data = ((BaseEntity<?>) (request.getDatas().get(0)))
				.toMap();
		List<String> updateFields = new ArrayList<String>();
		List<Object> updateParams = new ArrayList<Object>();
		Iterator<Entry<String, Object>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			updateFields.add(entry.getKey());
			updateParams.add(entry.getValue());
		}
		// param
		Object[] selectParam = SqlUtil.getSelectParams(request.getConditions());
		Object[] params = ArrayUtils
				.addAll(updateParams.toArray(), selectParam);

		// sql
		String sql = SqlUtil.getUpdateSql(dbType, request.getTable(),
				updateFields, request.getConditions());
		jdbcTemplate.update(sql.toString(), params);

	}

	public <T extends IEntity<?>> void delete(EntityRequest<T> request) {
		if (request.getDatas() == null || request.getDatas().isEmpty()) {
			// TODO throw exception
			return;
		}
		Object id = ((IEntity<?>) request.getDatas().get(0)).getId();
		String keyName = ((IEntity<?>) request.getDatas().get(0)).getKey();

		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		ConditionBean condition = new ConditionBean();
		condition.setName(keyName);
		condition.addValue(id);
		conditions.add(condition);

		// param
		Object[] param = SqlUtil.getSelectParams(conditions);
		// sql
		String sql = SqlUtil.getDeleteSql(dbType, request.getTable(),
				conditions);
		jdbcTemplate.update(sql.toString(), param);
	}

	public <T extends IEntity<?>> void insert(EntityRequest<T> request) {

		List<T> entitys = request.getDatas();
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		for (T _entity : entitys) {
			datas.add(((IEntity<?>) _entity).toMap());

		}
		List<Object> insertParam = new ArrayList<Object>();
		List<String> insertFields = new ArrayList<String>();
		for (Map<String, Object> data : datas) {
			insertParam.clear();
			insertFields.clear();
			Iterator<String> it = data.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				Object value = data.get(key);
				if (value == null) {
					continue;
				}
				insertFields.add(key);
				insertParam.add(value);
			}
			String sql = SqlUtil.getInsertSql(dbType, request.getTable(),
					insertFields);
			jdbcTemplate.update(sql, insertParam.toArray());
		}

	}

}
