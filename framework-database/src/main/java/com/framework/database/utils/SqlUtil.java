package com.framework.database.utils;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.framework.common.utils.ReflectUtil;
import com.framework.database.beans.ConditionBean;
import com.framework.database.beans.PageBean;
import com.framework.database.beans.ValueBean;
import com.framework.database.enums.DBTypeEnum;

public class SqlUtil {

	public static String getMaxFieldSql(DBTypeEnum dbType, String table,
			String field) {
		// oracle
		if (dbType.equals(DBTypeEnum.ORACLE)) {
			return "select max(" + field + ") from " + table;
		} else {
			return null;
		}
	}

	public static String getTablesSql(DBTypeEnum dbType, String dbName) {
		// oracle
		if (dbType.equals(DBTypeEnum.ORACLE)) {
			return "select  a.TABLE_NAME,a.TABLE_COMMENTS,a.COLUMN_NAME,a.COLUMN_COMMENTS,a.DATA_LENGTH,a.NULLABLE,a.DATA_SCALE,a.DATA_TYPE, b.pk  "
					+ " from (select b.COMMENTS column_comments, b.table_comments, a.*"
					+ " from user_tab_columns a"
					+ " left join (select a.*, b.COMMENTS table_comments"
					+ " from user_col_comments a"
					+ "  left join user_tab_comments b"
					+ " on a.TABLE_NAME = b.TABLE_NAME) b "
					+ " on a.TABLE_NAME || a.COLUMN_NAME = b.TABLE_NAME || b.COLUMN_NAME) a"
					+ " left join(　select b.column_name PK, b.table_name 　　from user_constraints a, user_cons_columns b 　　where a.constraint_name = b.constraint_name 　　and a.constraint_type = 'P') b"
					+ "  on a.TABLE_NAME = b.TABLE_NAME ";
		}
		// mysql
		else if (dbType.equals(DBTypeEnum.MYSQL)) {
			return " SELECT a.table_name,"
					+ "        b.table_comment            table_comments,"
					+ "        a.column_name,"
					+ "        a.column_comment           column_comments,"
					+ "        a.character_maximum_length data_length,"
					+ "        a.is_nullable              nullable,"
					+ "        a.numeric_scale            DATA_SCALE,"
					+ "        a.column_type              data_type,"
					+ "        a.column_key               pk"
					+ "   FROM INFORMATION_SCHEMA.COLUMNS a"
					+ "   LEFT JOIN INFORMATION_SCHEMA.TABLES b"
					+ "     ON a.table_name = b.table_name"
					+ "  WHERE a.table_schema = '" + dbName + "'";
		}
		// sqlserver
		else if (dbType.equals(DBTypeEnum.MSSQL)) {
			return null;
		} else {
			return null;
		}
	}

	public static String getWhere(DBTypeEnum dbType,
			List<ConditionBean> conditions) {
		List<String> sqlList = new ArrayList<String>();
		String treeSql = "";
		// List<String> treeSqlList = new ArrayList<String>();

		if (conditions != null) {
			for (ConditionBean condition : conditions) {

				StringBuffer sql = new StringBuffer();
				sql.append(getConditionName(dbType, condition));
				sql.append(" ");
				sql.append(getConditionValue(dbType, condition));
				sql.append(" ");
				sql.append(getConditionRule(dbType, condition));

				switch (condition.getCondition()) {
				case TREEEQ:
				case TREEIN:
					treeSql = sql.toString();
					break;
				default:
					sqlList.add(sql.toString());
					break;
				}

			}
			return "where " + StringUtils.join(sqlList, " and ") + treeSql;

		} else {
			return "";
		}

	}

	public static String getConditionName(DBTypeEnum dbType,
			ConditionBean condition) {

		String name = condition.getName();
		switch (condition.getCondition()) {
		case TODAY:
			// oracle
			if (dbType.equals(DBTypeEnum.ORACLE)) {
				return "trunc(" + name + ")";
			}
			// mysql
			else if (dbType.equals(DBTypeEnum.MYSQL)) {
				return "DATE(" + name + ")";
			}
		case TREEEQ:
			return "start with " + name;
		case TREEIN:
			return "start with " + name;
		default:
			return name;
		}
	}

	public static String getConditionValue(DBTypeEnum dbType,
			ConditionBean condition) {
		switch (condition.getCondition()) {
		case NOTEQ:
			return "!= ?";
		case EQ:
			return "= ?";
		case GE:
			return ">= ?";
		case GT:
			return "> ?";
		case LE:
			return "<= ?";
		case LT:
			return "< ?";
		case LIKE:
			// selectValue = " like '%?%' ";
			return "like '%'||?||'%'";
		case LLIKE:
			// selectValue = " like '?%' ";
			return "like ?||'%'";
		case RLIKE:
			// selectValue = " like '%?' ";
			return " like '%'||? ";
		case ISNULL:
			return "is null";
		case NOTNULL:
			return "is not null";
		case TREEIN:
		case IN:
			int valueSize = condition.getValues().size();
			List<String> _values = new ArrayList<String>();
			for (int i = 0; i < valueSize; i++) {
				_values.add("?");
			}
			return "in (" + StringUtils.join(_values, ",") + ")";
		case TODAY:

			// oracle
			if (dbType.equals(DBTypeEnum.ORACLE)) {
				return "= trunc(sysdate)";
			}
			// mysql
			else if (dbType.equals(DBTypeEnum.MYSQL)) {
				return "= CURDATE()";
			}
		case TREEEQ:
			return "= ?";

		default:
			return "= ?";
		}
	}

	public static String getConditionRule(DBTypeEnum dbType,
			ConditionBean condition) {
		switch (condition.getCondition()) {
		case TREEEQ:
			return "connect by " + condition.getTreePid() + " = prior "
					+ condition.getTreeId();
		case TREEIN:
			return "connect by " + condition.getTreePid() + " = prior "
					+ condition.getTreeId();
		default:
			return "";
		}
	}

	/**
	 * 获取查询的字段
	 * 
	 * @param fieldMap
	 * @param fields
	 * @return
	 */
	public static String getSelectFieldSql(List<String> fields) {
		if (fields == null || fields.isEmpty()) {
			return "*";
		} else {
			return StringUtils.join(fields, ",");
		}
	}

	public static String getPageSql(DBTypeEnum dbType, String sql,
			PageBean<?> page) {
		int start = (page.getPageIndex() - 1) * page.getRecordNumber();
		int end = start + page.getRecordNumber();
		StringBuffer sqlBuffer = new StringBuffer();
		// oracle
		if (dbType.equals(DBTypeEnum.ORACLE)) {
			sqlBuffer.append("select * from (select rownum rn, a.* from (");
			sqlBuffer.append(sql);
			sqlBuffer.append(") a where rownum <=" + end + ")  where rn > "
					+ start);
		}
		// mysql
		else if (dbType.equals(DBTypeEnum.MYSQL)) {
			sqlBuffer.append("select * from (");
			sqlBuffer.append(sql);
			sqlBuffer.append(") a  LIMIT " + start + ","
					+ page.getRecordNumber());
		} else {
			sqlBuffer.append(sql);
		}
		// TODO OUTPRING TO DEBUG
		System.out.println(sqlBuffer.toString());
		return sqlBuffer.toString();
	}

	public static String getCountSql(String sql) {
		StringBuffer sqlBuffer = new StringBuffer(
				"select count(*) count from (");
		sqlBuffer.append(sql);
		sqlBuffer.append(") a");
		// TODO OUTPRING TO DEBUG
		System.out.println(sqlBuffer.toString());
		return sqlBuffer.toString();
	}

	/**
	 * 获取查询语句
	 * 
	 * @param dbType
	 * @param request
	 * @return
	 */
	public static String getSelectSql(DBTypeEnum dbType, String table,
			List<String> fields, List<ConditionBean> conditions) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		sql.append(getSelectFieldSql(fields));
		sql.append(" from ");
		sql.append(table);
		sql.append(" ");
		sql.append(getWhere(dbType, conditions));
		// TODO OUTPRING TO DEBUG
		System.out.println(sql.toString());
		return sql.toString();
	}

	/**
	 * 获取删除语句
	 * 
	 * @param dbType
	 * @param request
	 * @return
	 */
	public static String getDeleteSql(DBTypeEnum dbType, String table,
			List<ConditionBean> conditions) {

		// delete
		StringBuffer sql = new StringBuffer();
		sql.append("delete ");
		sql.append(table);
		sql.append(" ");
		sql.append(getWhere(dbType, conditions));
		// TODO OUTPRING TO DEBUG
		System.out.println(sql.toString());
		return sql.toString();
	}

	/**
	 * 获取更新语句
	 * 
	 * @param dbType
	 * @param request
	 * @param updateFields
	 * @return
	 */
	public static String getUpdateSql(DBTypeEnum dbType, String table,
			List<String> updateFields, List<ConditionBean> conditions) {
		// update
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		sql.append(table);
		sql.append(" set ");
		List<String> setList = new ArrayList<String>();
		for (String field : updateFields) {
			setList.add(field + " = ?");
		}
		sql.append(StringUtils.join(setList, " , "));
		sql.append(" ");
		sql.append(getWhere(dbType, conditions));
		// TODO OUTPRING TO DEBUG
		System.out.println(sql.toString());
		return sql.toString();

	}

	/**
	 * 获取新增语句
	 * 
	 * @param dbType
	 * @param request
	 * @param insertFields
	 * @return
	 */
	public static String getInsertSql(DBTypeEnum dbType, String table,
			List<String> insertFields) {
		// insert
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		sql.append(table);
		sql.append(" (");
		sql.append(StringUtils.join(insertFields, ","));
		sql.append(") ");
		sql.append("values");
		sql.append(" (");
		List<String> paramList = new ArrayList<String>();
		for (int i = 0; i < insertFields.size(); i++) {
			paramList.add("?");
		}
		sql.append(StringUtils.join(paramList, ","));
		sql.append(")");
		// TODO OUTPRING TO DEBUG
		System.out.println(sql.toString());
		return sql.toString();

	}

	public static Object[] getSelectParams(List<ConditionBean> conditions) {
		List<Object> params = new ArrayList<Object>();
		List<Object> backParams = new ArrayList<Object>();
		if (conditions != null && !conditions.isEmpty()) {
			for (ConditionBean condition : conditions) {
				List<Object> values = condition.getValues();
				if (values == null || values.isEmpty()) {
					continue;
				}
				switch (condition.getCondition()) {
				case TODAY:
					break;
				case TREEEQ:
					backParams.add(condition.getValues().get(0));
					break;
				case IN:
					for (Object inValue : condition.getValues()) {
						params.add(inValue);
					}
					break;
				case TREEIN:
					for (Object inValue : condition.getValues()) {
						backParams.add(inValue);
					}
					break;
				default:
					params.add(condition.getValues().get(0));
					break;
				}
			}
		}
		params.addAll(backParams);
		return params.toArray();
	}

	public static Map<String, ValueBean> getResult(ResultSet resultSet)
			throws SQLException {
		Map<String, ValueBean> result = new HashMap<String, ValueBean>();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String field = rsmd.getColumnName(i);
			ValueBean value = new ValueBean(resultSet.getObject(field));
			result.put(field, value);
		}
		return result;
	}

	public static <T> T getResult(ResultSet resultSet, Class<T> entityClass,
			Map<String, String> keyMapEntity) throws SQLException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException,
			NoSuchFieldException {
		T entityObject = entityClass.newInstance();
		ResultSetMetaData rsmd = resultSet.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String field = rsmd.getColumnName(i).toUpperCase();
			Object value = resultSet.getObject(field);
			String entityField = keyMapEntity.get(field);
			if (StringUtils.isNotEmpty(entityField)) {
				ReflectUtil.setFieldValue(entityObject, entityField, value);
			}
		}
		return entityObject;
	}

	public static List<ConditionBean> paramToCondition(Map<String, Object> param) {
		List<ConditionBean> conditions = new ArrayList<ConditionBean>();
		Iterator<Entry<String, Object>> paramIt = param.entrySet().iterator();
		while (paramIt.hasNext()) {
			Entry<String, Object> entry = paramIt.next();
			ConditionBean condition = new ConditionBean();
			condition.setName(entry.getKey());
			condition.addValue(entry.getValue());
			conditions.add(condition);
		}
		return conditions;
	}

}
