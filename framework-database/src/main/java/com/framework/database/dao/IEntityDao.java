package com.framework.database.dao;

import java.util.List;

import com.framework.database.beans.PageBean;
import com.framework.database.entity.IEntity;
import com.framework.database.request.EntityRequest;

public interface IEntityDao {
	/**
	 * 改变数据库
	 * 
	 * @param dbName
	 */
	public void changeDataBase(String dbName);

	/**
	 * 查询单条记录
	 * 
	 * @param request
	 * @return
	 */
	public <T extends IEntity<?>> T select(EntityRequest<T> request);

	/**
	 * 查询多条记录集
	 * 
	 * @param request
	 * @return
	 */
	public <T extends IEntity<?>> List<T> selectList(EntityRequest<T> request);

	/**
	 * 查询分页记录
	 * 
	 * @return
	 */
	public <T extends IEntity<?>> PageBean<T> selectPage(
			EntityRequest<T> request);

	/**
	 * 更新记录集
	 * 
	 * @param request
	 */
	public <T extends IEntity<?>> void update(EntityRequest<T> request);

	/**
	 * 插入记录集
	 * 
	 * @param request
	 */
	public <T extends IEntity<?>> void insert(EntityRequest<T> request);

	/**
	 * 根据实体KEY删除数据
	 * 
	 * @param request
	 */
	public <T extends IEntity<?>> void delete(EntityRequest<T> request);
}
