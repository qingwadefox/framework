package com.framework.database.entity.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;

import com.framework.common.utils.ReflectUtil;
import com.framework.database.entity.IEntity;
import com.framework.database.sequence.ISequenceCreate;
import com.framework.database.stereotypes.Column;
import com.framework.database.stereotypes.Table;
import com.framework.database.utils.EntityUtil;

public class BaseEntity<T extends Serializable> implements IEntity<T> {

	private static final long serialVersionUID = 1L;
	private Field _idField;
	private ISequenceCreate<T> sequenceCreate;

	public BaseEntity() {
		Field[] fields = ReflectUtil.getFields(this.getClass());
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null && column.id()) {
				_idField = field;
				break;
			}
		}
	}

	public void setId(T id) {
		if (_idField == null) {
			return;
		}
		try {
			ReflectUtil.setFieldValue(this, _idField, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public T getId() {
		if (_idField == null) {
			return null;
		}
		try {
			return ((T) ReflectUtil.getFieldValue(this, _idField));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getTable() {
		return this.getClass().getAnnotation(Table.class).value();
	}

	public Map<String, Object> toMap() {
		return EntityUtil.entityToMap(this);
	}

	public String getKey() {
		if (_idField == null) {
			return null;
		}
		return this._idField.getAnnotation(Column.class).value();
	}

	public Object get(String key) {
		try {
			return ReflectUtil.getFieldValue(this, key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void set(String key, Object value) {
		try {
			ReflectUtil.setFieldValue(this, key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSequeneceCreate(ISequenceCreate<T> sequenceCreate) {
		this.sequenceCreate = sequenceCreate;
	}

	public ISequenceCreate<T> getSequeneceCreate() {
		return null;
	}

	public void initialize() {
		if (sequenceCreate != null) {
			setId(sequenceCreate.next());
		}
	}

}
