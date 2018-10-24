package com.framework.database.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.framework.common.utils.RandomUtil;
import com.framework.database.enums.DBTypeEnum;
import com.framework.database.enums.SqlClassEnum;

public class DataBaseUtil {

	public static String getUrl(DBTypeEnum dbType, String ip, String port,
			String name) {
		StringBuffer url = new StringBuffer();

		// oracle()
		if (dbType.equals(DBTypeEnum.ORACLE)) {
			url.append("jdbc:oracle:thin:@");
			url.append(ip);
			url.append(":");
			url.append(port);
			url.append(":");
			url.append(name);
			return url.toString();
		}
		// mysql
		else if (dbType.equals(DBTypeEnum.MYSQL)) {
			url.append("jdbc:mysql://");
			url.append(ip);
			url.append(":");
			url.append(port);
			url.append("/");
			url.append(name);
			url.append("?useUnicode=true&characterEncoding=utf8");
			return url.toString();
		}
		// sqlserver
		else if (dbType.equals(DBTypeEnum.MSSQL)) {
			return null;
		} else {
			return null;
		}
	}

	public static String getDriverClass(DBTypeEnum dbType) {
		// oracle
		if (dbType.equals(DBTypeEnum.ORACLE)) {
			return "oracle.jdbc.driver.OracleDriver";
		}
		// mysql
		else if (dbType.equals(DBTypeEnum.MYSQL)) {
			return "com.mysql.jdbc.Driver";
		}
		// sqlserver
		else if (dbType.equals(DBTypeEnum.MSSQL)) {
			return null;
		} else {
			return null;
		}
	}

	public static String createKeyValue(SqlClassEnum dt) {
		switch (dt) {
		case STRING:
			return RandomUtil.getUUID();
		default:
			return RandomUtil.getUUID();
		}
	}

	public static Connection createConnection(DBTypeEnum dbType, String ip,
			String port, String name, String username, String password)
			throws ClassNotFoundException, SQLException {

		Class.forName(getDriverClass(dbType));
		Connection con = DriverManager.getConnection(
				getUrl(dbType, ip, port, username), username, password);
		return con;

	}

}
