package com.framework.plugin.web.common.server.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.framework.common.utils.StrUtil;
import com.framework.plugin.web.common.web.spring.factory.DataBaseFactory;

@Repository
public class SqlDao {

    @Autowired
    private DataBaseFactory dataBaseFactory;

    private JdbcTemplate getJdbcTemplate() {
        return dataBaseFactory.getJdbcTemplate();
    }

    public List<Map<String, String>> select(String sql) {

        return this.getJdbcTemplate().query(sql.toString(),
                new RowMapper<Map<String, String>>() {
                    @Override
                    public Map<String, String> mapRow(ResultSet arg0, int arg1)
                            throws SQLException {
                        Map<String, String> result = new HashMap<String, String>();
                        ResultSetMetaData rsmd = arg0.getMetaData();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            String field = rsmd.getColumnName(i);
                            if (field.equals("CREATE_DATE")) {
                                System.out.println(StrUtil.toString(arg0
                                        .getObject(field)));
                            }
                            result.put(field,
                                    StrUtil.toString(arg0.getObject(field)));
                        }

                        return result;
                    }
                });
    }

    public List<Map<String, String>> select(String sql, Object[] param) {
        return this.getJdbcTemplate().query(sql.toString(),
                new RowMapper<Map<String, String>>() {
                    @Override
                    public Map<String, String> mapRow(ResultSet arg0, int arg1)
                            throws SQLException {
                        Map<String, String> result = new HashMap<String, String>();
                        ResultSetMetaData rsmd = arg0.getMetaData();
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                            String field = rsmd.getColumnName(i);
                            result.put(field,
                                    StrUtil.toString(arg0.getObject(field)));
                        }

                        return result;
                    }
                }, param);
    }
}
