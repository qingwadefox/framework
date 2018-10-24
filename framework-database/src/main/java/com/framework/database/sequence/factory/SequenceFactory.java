package com.framework.database.sequence.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.framework.database.entity.IEntity;
import com.framework.database.sequence.ISequenceCreate;

public class SequenceFactory {

	private static Map<String, ISequenceCreate<Serializable>> seqCreateMap;

	static {
		if (seqCreateMap == null) {
			seqCreateMap = new HashMap<String, ISequenceCreate<Serializable>>();
		}
	}

	public static <T extends Serializable> ISequenceCreate<Serializable> getSequenceCreate(
			Class<T> clazz, String table) {
		return seqCreateMap.get(table);
	}

	public static <T extends Serializable> ISequenceCreate<Serializable> getSequenceCreate(
			IEntity<T> entry) {
		String table = entry.getTable();
		return getSequenceCreate(entry.getId().getClass(), table);
	}

}
