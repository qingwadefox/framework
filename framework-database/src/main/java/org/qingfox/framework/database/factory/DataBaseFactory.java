package org.qingfox.framework.database.factory;

import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.qingfox.framework.database.beans.DataBaseBean;
import org.qingfox.framework.database.beans.FieldBean;
import org.qingfox.framework.database.beans.TableBean;
import org.qingfox.framework.database.enums.DBTypeEnum;
import org.qingfox.framework.database.manager.SequenceManager;
import org.qingfox.framework.database.utils.DataBaseUtil;
import org.qingfox.framework.database.utils.SqlUtil;

//import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import org.qingfox.framework.common.log.ILogger;
import org.qingfox.framework.common.log.LoggerFactory;

public class DataBaseFactory {

	private static final ILogger logger = LoggerFactory.getLogger(DataBaseFactory.class);

	public final static String DEFAULT_DBNAME = "default";
	public static String NOW_DBNAME = DEFAULT_DBNAME;

	private static Map<String, DataBaseBean> dataBaseMaps;

	public static void init(DBTypeEnum dbType, String ip, String port, String name, String username, String password) throws PropertyVetoException {
		init(DEFAULT_DBNAME, dbType, ip, port, name, username, password);
	}

	/**
	 * 改变数据库
	 * 
	 * @param dbName
	 */
	public static void changeDataBase(String dbName) {
		if (dataBaseMaps.get(dbName) == null) {
			// TODO throw exception
			return;
		}
		NOW_DBNAME = dbName;
	}

	public static void init(String dbName, DBTypeEnum dbType, String ip, String port, String name, String username, String password) throws PropertyVetoException {
		logger.info("获取到数据库连接参数dbName【", dbName, "】，dbType【", dbType.getValue(), "】，ip【", ip, "】，port【", port, "】，name【", name, "】，username【", username, "】，password【", password, "】");
		if (dataBaseMaps == null) {
			dataBaseMaps = new HashMap<String, DataBaseBean>();
		}
		DataBaseBean dataBase = new DataBaseBean();
		dataBase.setDbType(dbType);
		dataBase.setIp(ip);
		dataBase.setPort(port);
		dataBase.setName(name);
		dataBase.setUsername(username);
		dataBase.setPassword(password);

		// 初始化数据源
		DruidDataSource ds = new DruidDataSource();
		// ComboPooledDataSource ds = new ComboPooledDataSource();
		String driverClass = DataBaseUtil.getDriverClass(dbType);
		String url = DataBaseUtil.getUrl(dbType, ip, port, name);
		ds.setDriverClassName(driverClass);
		ds.setUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		// ds.setDriverClass(driverClass);
		// ds.setJdbcUrl(url);
		// ds.setUser(username);
		// ds.setPassword(password);
		dataBase.setDataSource(ds);

		// 初始化spring-jdbc
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		jdbcTemplate.setDataSource(ds);
		dataBase.setJdbcTemplate(jdbcTemplate);

		// 初始化事物管理
		DataSourceTransactionManager tm = new DataSourceTransactionManager();
		tm.setDataSource(ds);

		// 初始化序列管理
		SequenceManager sm = new SequenceManager();
		dataBase.setSequenceManager(sm);

		// 初始化数据库表信息
		dataBase.setTablesMap(getTables(jdbcTemplate, dbType, username));

		dataBaseMaps.put(dbName, dataBase);
	}

	public static JdbcTemplate getJdbcTemplate() {
		return getJdbcTemplate(NOW_DBNAME);
	}

	public static SequenceManager getSequenceManager() {
		return getSequenceManager(NOW_DBNAME);
	}

	public static JdbcTemplate getJdbcTemplate(String dbName) {
		DataBaseBean dataBase = dataBaseMaps.get(dbName);
		if (dataBase != null) {
			return dataBase.getJdbcTemplate();
		} else {
			return null;
		}
	}

	public static Map<String, TableBean> getTablesMap(String dbName) {
		DataBaseBean dataBase = dataBaseMaps.get(dbName);
		if (dataBase != null) {
			return dataBase.getTablesMap();
		} else {
			return null;
		}
	}

	public static SequenceManager getSequenceManager(String dbName) {
		DataBaseBean dataBase = dataBaseMaps.get(dbName);
		if (dataBase != null) {
			return dataBase.getSequenceManager();
		} else {
			return null;
		}
	}

	public static DBTypeEnum getDBType() {
		return getDBType(NOW_DBNAME);
	}

	public static DBTypeEnum getDBType(String dbName) {
		DataBaseBean dataBase = dataBaseMaps.get(dbName);
		if (dataBase != null) {
			return dataBase.getDbType();
		} else {
			return null;
		}

	}

	private static Map<String, TableBean> getTables(JdbcTemplate jdbcTemplate, DBTypeEnum dbType, String name) {
		final Map<String, TableBean> tablesMap = new HashMap<String, TableBean>();
		String sql = SqlUtil.getTablesSql(dbType, name);
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String tableName = rs.getString("TABLE_NAME").toUpperCase();
				String pkName = rs.getString("PK");
				TableBean tableBean = tablesMap.get(tableName);

				if (tableBean == null) {
					tableBean = new TableBean();
					tableBean.setName(tableName);
					tableBean.setComments(rs.getString("TABLE_COMMENTS"));
					tableBean.setFields(new LinkedHashMap<String, FieldBean>());
				}

				LinkedHashMap<String, FieldBean> fieldMap = tableBean.getFields();

				FieldBean fieldBean = new FieldBean();
				fieldBean.setName(rs.getString("COLUMN_NAME").toUpperCase());
				fieldBean.setComments(rs.getString("COLUMN_COMMENTS"));
				fieldBean.setLength(rs.getInt("DATA_LENGTH"));
				fieldBean.setNullable(rs.getString("NULLABLE") == "Y" ? true : false);
				fieldBean.setScale(rs.getInt("DATA_SCALE"));
				fieldBean.setType(rs.getString("DATA_TYPE"));
				fieldMap.put(fieldBean.getName(), fieldBean);
				tableBean.setFields(fieldMap);
				if (fieldBean.getName().equals(pkName)) {
					tableBean.setKeyfield(fieldBean);
				}
				tablesMap.put(tableName, tableBean);

			}
		});

		// TODO SEQ
		// for (TableBean table : tablesMap.values()) {
		// Map<String, SequenceInf> seqMap = SpringUtil.getSequence(table
		// .getName());
		//
		// if (table.getKeyfield() != null
		// && seqMap.get(table.getKeyfield().getName()) == null) {
		//
		// // number
		// if (DatetypeEnum.getEnmu(table.getKeyfield().getType()).equals(
		// DatetypeEnum.NUMBER)) {
		// NumberSequence numberSequence = new NumberSequence();
		// numberSequence
		// .setCurrval(String.valueOf(this.getJdbcTemplate()
		// .queryForLong(
		// DataBaseUtil.getMaxFieldSql(this
		// .getType(),
		// table.getName(), table
		// .getKeyfield()
		// .getName()))));
		// seqMap.put(table.getKeyfield().getName(), numberSequence);
		// }
		//
		// }
		// table.setSequenceMap(seqMap);
		// }

		return tablesMap;

	}
}
