package com.framework.database.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.framework.database.beans.PageBean;
import com.framework.database.beans.ValueBean;
import com.framework.database.dao.IMapDao;
import com.framework.database.enums.DBTypeEnum;
import com.framework.database.factory.DataBaseFactory;
import com.framework.database.request.MapRequest;
import com.framework.database.utils.SqlUtil;

public class MapDaoImpl implements IMapDao {

	private JdbcTemplate jdbcTemplate;
	private DBTypeEnum dbType;

	public MapDaoImpl() {
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
	}

	public void changeDataBase(String dbName) {
		DataBaseFactory.changeDataBase(dbName);
		jdbcTemplate = DataBaseFactory.getJdbcTemplate();
		dbType = DataBaseFactory.getDBType();
	}

	public ValueBean selectValue(MapRequest request) {
		Map<String, ValueBean> resultMap = this.select(request);
		if (resultMap == null || resultMap.isEmpty()) {
			return null;
		}
		String field = null;
		List<String> fields = request.getFields();
		if (fields != null && !fields.isEmpty()) {
			field = fields.get(0);
		} else {
			field = resultMap.keySet().iterator().next();
		}
		return resultMap.get(field.toUpperCase());
	}

	public Map<String, ValueBean> select(MapRequest request) {
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
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
				}, param);
		if ((list != null && !list.isEmpty())) {
			return list.get(0);
		} else
			return null;
	}

	public List<Map<String, ValueBean>> selectList(MapRequest request) {
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
		return jdbcTemplate.query(sql.toString(),
				new RowMapper<Map<String, ValueBean>>() {
					public Map<String, ValueBean> mapRow(ResultSet arg0,
							int arg1) throws SQLException {
						return SqlUtil.getResult(arg0);
					}
				}, param);
	}

	public List<ValueBean> selectValueList(MapRequest request) {
		List<Map<String, ValueBean>> resultList = this.selectList(request);
		if (resultList == null || resultList.isEmpty()) {
			return null;
		}
		String field = null;
		List<String> fields = request.getFields();
		if (fields != null && !fields.isEmpty()) {
			field = fields.get(0);
		} else {
			field = resultList.get(0).keySet().iterator().next();
		}
		List<ValueBean> valueList = new ArrayList<ValueBean>();
		for (Map<String, ValueBean> resultMap : resultList) {
			valueList.add(resultMap.get(field));
		}
		return valueList;
	}

	public void delete(MapRequest request) {
		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());
		// sql
		String sql = SqlUtil.getDeleteSql(dbType, request.getTable(),
				request.getConditions());
		jdbcTemplate.update(sql.toString(), param);
	}

	public void update(MapRequest request) {
		if (request.getDatas() == null || request.getDatas().isEmpty()) {
			// TODO throw exception
			return;
		}
		Map<String, Object> data = request.getDatas().get(0);
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

	public void insert(MapRequest request) {
		List<Map<String, Object>> datas = request.getDatas();

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
				insertParam.add(data.get(key));
			}
			String sql = SqlUtil.getInsertSql(dbType, request.getTable(),
					insertFields);
			jdbcTemplate.update(sql, insertParam.toArray());
		}

	}

	public PageBean<Map<String, ValueBean>> selectPage(MapRequest request) {

		PageBean<Map<String, ValueBean>> page = new PageBean<Map<String, ValueBean>>();
		page.setPageIndex(request.getPage().getPageIndex());
		page.setRecordNumber(request.getPage().getRecordNumber());

		// param
		Object[] param = SqlUtil.getSelectParams(request.getConditions());

		// select sql
		String sql = SqlUtil.getSelectSql(dbType, request.getTable(),
				request.getFields(), request.getConditions());
		String pageSql = SqlUtil.getPageSql(dbType, sql, request.getPage());
		String countSql = SqlUtil.getCountSql(sql);

		List<Map<String, ValueBean>> records = jdbcTemplate.query(pageSql,
				new RowMapper<Map<String, ValueBean>>() {
					public Map<String, ValueBean> mapRow(ResultSet arg0,
							int arg1) throws SQLException {
						return SqlUtil.getResult(arg0);
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

}
