package com.framework.plugin.web.common.web.spring.factory;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.stereotype.Component;

import com.framework.common.utils.ClassUtil;
import com.framework.common.utils.DataBaseUtil;
import com.framework.common.utils.PropertiesUtil;
import com.framework.plugin.web.common.server.sequences.NumberSequence;
import com.framework.plugin.web.common.web.spring.ConfigPath;
import com.framework.plugin.web.common.web.spring.utils.SpringUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;

@Component
public class DataBaseFactory implements InitializingBean {

    @Autowired
    private SpringFactory springFactory;

    private JdbcTemplate jdbcTemplate;

    private SessionFactory sessionFactory;

    private boolean run = false;

    private String type;

    private String port;

    private String name;

    private String username;

    private String password;

    private String ip;

    private Map<String, TableBean> tableBeanMap;

    public void initDataBaseConfig() throws PropertyVetoException, IOException {
        File file = new File(ClassUtil.getClassPath()).getParentFile();
        file = new File(file.getPath() + File.separator
                + ConfigPath.DATABASECONFIG_PATH);
        if (file.exists()) {
            Properties p = PropertiesUtil.loadProperties(file);
            type = p.getProperty("type");
            port = p.getProperty("port");
            name = p.getProperty("name");
            password = p.getProperty("password");
            ip = p.getProperty("ip");
            username = p.getProperty("username");

            // 数据库设置是否为空
            if (StringUtils.isEmpty(type) || StringUtils.isEmpty(ip)
                    || StringUtils.isEmpty(port) || StringUtils.isEmpty(name)) {
                return;
            }
            ComboPooledDataSource cds = new ComboPooledDataSource();
            cds.getConnection().
            String driverClass = DataBaseUtil.getDriverClass(type);
            String url = DataBaseUtil.getUrl(type, ip, port, name);
            cds.setDriverClass(driverClass);
            cds.setJdbcUrl(url);
            cds.setUser(username);
            cds.setPassword(password);
            springFactory.setBean("dataSource", cds);

            jdbcTemplate = new JdbcTemplate();
            jdbcTemplate.setDataSource(cds);
            springFactory.setBean("jdbcTemplate", jdbcTemplate);

            LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
            Properties hp = new Properties();
            hp.put("hibernate.show_sql", "show");
            hp.put("hibernate.dialect",
                    "org.hibernate.dialect.Oracle10gDialect");
            hp.put("hibernate.max_fetch_depth", "2");
            hp.put("hibernate.jdbc.fetch_size", "50");
            hp.put("hibernate.jdbc.batch_size", "50");
            hp.put("hibernate.bytecode.use_reflection_optimizer", "true");
            hp.put("hibernate.format_sql", "true");
            hp.put("hibernate.c3p0.max_statements", "0");
            lsfb.setHibernateProperties(hp);
            lsfb.setPackagesToScan("com");
            lsfb.setDataSource(cds);
            lsfb.afterPropertiesSet();
            sessionFactory = lsfb.getObject();
            springFactory.setBean("sessionFactory", lsfb);

            HibernateTransactionManager htm = new HibernateTransactionManager();
            htm.setSessionFactory(lsfb.getObject());
            springFactory.setBean("transactionManager", htm);
            run = true;
        } else {
            run = false;
        }
    }

    @SuppressWarnings({ "deprecation" })
    public void initDataBaseTableBeanMap() {
        tableBeanMap = new HashMap<String, TableBean>();
        String sql = DataBaseUtil.getTableBeanSql(type, name);
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String tableName = rs.getString("TABLE_NAME").toUpperCase();
                String pkName = rs.getString("PK");
                TableBean tableBean = tableBeanMap.get(tableName);

                if (tableBean == null) {
                    tableBean = new TableBean();
                    tableBean.setName(tableName);
                    tableBean.setComments(rs.getString("TABLE_COMMENTS"));
                    tableBean.setFields(new LinkedHashMap<String, FieldBean>());
                }

                LinkedHashMap<String, FieldBean> fieldMap = tableBean
                        .getFields();

                FieldBean fieldBean = new FieldBean();
                fieldBean.setName(rs.getString("COLUMN_NAME").toUpperCase());
                fieldBean.setComments(rs.getString("COLUMN_COMMENTS"));
                fieldBean.setLength(rs.getInt("DATA_LENGTH"));
                fieldBean.setNullable(rs.getString("NULLABLE") == "Y" ? true
                        : false);
                fieldBean.setScale(rs.getInt("DATA_SCALE"));
                fieldBean.setType(rs.getString("DATA_TYPE"));
                fieldMap.put(fieldBean.getName(), fieldBean);
                tableBean.setFields(fieldMap);
                if (fieldBean.getName().equals(pkName)) {
                    tableBean.setKeyfield(fieldBean);
                }
                tableBeanMap.put(tableName, tableBean);

            }
        });

        for (TableBean table : tableBeanMap.values()) {
            Map<String, SequenceInf> seqMap = SpringUtil.getSequence(table
                    .getName());

            if (table.getKeyfield() != null
                    && seqMap.get(table.getKeyfield().getName()) == null) {

                // number
                if (DatetypeEnum.getEnmu(table.getKeyfield().getType()).equals(
                        DatetypeEnum.NUMBER)) {
                    NumberSequence numberSequence = new NumberSequence();
                    numberSequence
                            .setCurrval(String.valueOf(this.getJdbcTemplate()
                                    .queryForLong(
                                            DataBaseUtil.getMaxFieldSql(this
                                                    .getType(),
                                                    table.getName(), table
                                                            .getKeyfield()
                                                            .getName()))));
                    seqMap.put(table.getKeyfield().getName(), numberSequence);
                }

            }
            table.setSequenceMap(seqMap);
        }

    }

    @Override
    public void afterPropertiesSet() {
        try {
            initDataBaseConfig();
        } catch (Exception e) {
            e.printStackTrace();
            run = false;
        }

        if (run) {
            initDataBaseTableBeanMap();
        }
    }

    public boolean isRun() {
        return run;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public String getType() {
        return type;
    }

    public SpringFactory getSpringFactory() {
        return springFactory;
    }

    public String getPort() {
        return port;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIp() {
        return ip;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, TableBean> getTableBeanMap() {
        return tableBeanMap;
    }

}
