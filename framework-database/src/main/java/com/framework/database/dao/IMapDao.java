package com.framework.database.dao;

import java.util.List;
import java.util.Map;

import com.framework.database.beans.PageBean;
import com.framework.database.beans.ValueBean;
import com.framework.database.request.MapRequest;

public interface IMapDao {
	/**
	 * 改变数据库
	 * 
	 * @param dbName
	 */
	public void changeDataBase(String dbName);

	/**
	 * 查询单个字段
	 * 
	 * @param request
	 * @return
	 */
	public ValueBean selectValue(MapRequest request);

	/**
	 * 查询单条记录
	 * 
	 * @param request
	 * @return
	 */
	public Map<String, ValueBean> select(MapRequest request);

	/**
	 * 查询多条记录集
	 * 
	 * @param request
	 * @return
	 */
	public List<Map<String, ValueBean>> selectList(MapRequest request);

	/**
	 * 查询分页记录
	 * 
	 * @return
	 */
	public PageBean<Map<String, ValueBean>> selectPage(MapRequest request);

	/**
	 * 查询多个字段记录集
	 * 
	 * @param request
	 * @return
	 */
	public List<ValueBean> selectValueList(MapRequest request);

	/**
	 * 删除记录集
	 * 
	 * @param request
	 */
	public void delete(MapRequest request);

	/**
	 * 更新记录集
	 * 
	 * @param request
	 */
	public void update(MapRequest request);

	/**
	 * 插入记录集
	 * 
	 * @param request
	 */
	public void insert(MapRequest request);
}
