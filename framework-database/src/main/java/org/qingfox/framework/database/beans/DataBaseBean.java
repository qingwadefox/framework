package org.qingfox.framework.database.beans;

import java.io.Serializable;
import java.util.Map;

import javax.sql.DataSource;

import org.qingfox.framework.database.enums.DBTypeEnum;
import org.qingfox.framework.database.manager.SequenceManager;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataBaseBean implements Serializable {

	private static final long serialVersionUID = 3181088016693573002L;

	private DBTypeEnum dbType;
	private String ip;
	private String port;
	private String name;
	private String username;
	private String password;
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private Map<String, TableBean> tablesMap;
	private SequenceManager sequenceManager;

	public DBTypeEnum getDbType() {
		return dbType;
	}

	public void setDbType(DBTypeEnum dbType) {
		this.dbType = dbType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Map<String, TableBean> getTablesMap() {
		return tablesMap;
	}

	public void setTablesMap(Map<String, TableBean> tablesMap) {
		this.tablesMap = tablesMap;
	}

	public SequenceManager getSequenceManager() {
		return sequenceManager;
	}

	public void setSequenceManager(SequenceManager sequenceManager) {
		this.sequenceManager = sequenceManager;
	}

}
