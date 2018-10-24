package org.qingfox.framework.database.entity;

import java.io.Serializable;
import java.util.Map;

import org.qingfox.framework.database.sequence.ISequenceCreate;

public interface IEntity<T extends Serializable> extends Serializable {

	/**
	 * 初始化实体
	 */
	public void initialize();

	/**
	 * 获取映射表名
	 * 
	 * @return
	 */
	public String getTable();

	/**
	 * 获取逐渐字段
	 * 
	 * @return
	 */
	public String getKey();

	/**
	 * 设置主键
	 * 
	 * @param id
	 */
	public void setId(T id);

	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public T getId();

	/**
	 * 实体转化为MAP
	 * 
	 * @return
	 */
	public Map<String, Object> toMap();

	/**
	 * 根据字段名称获取字
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key);

	/**
	 * 根据字段名称设置值
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, Object value);

	/**
	 * 设置序列创建类
	 * 
	 * @param sequenceCreate
	 */
	public void setSequeneceCreate(ISequenceCreate<T> sequenceCreate);

	/**
	 * 获取序列创建类
	 */
	public ISequenceCreate<T> getSequeneceCreate();

}
