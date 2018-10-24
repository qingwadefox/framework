package com.framework.plugin.web.mvc.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.framework.common.beans.ConditionBean;
import com.framework.common.beans.FieldBean;
import com.framework.plugin.web.common.bean.PageBean;
import com.framework.plugin.web.common.bean.TableBean;
import com.framework.common.enums.DatetypeEnum;
import com.framework.plugin.web.common.web.spring.factory.DataBaseFactory;
import com.framework.plugin.web.common.server.request.CRUDRequest;
import com.framework.plugin.web.common.server.sequences.inf.SequenceInf;
import com.framework.common.enums.ConditionEnum;
import com.framework.common.utils.DataBaseUtil;
import com.framework.common.utils.DateUtil;
import com.framework.common.utils.StrUtil;

@Repository
public class CRUDDao {

    @Autowired
    private DataBaseFactory dataBaseFactory;

    private Map<String, TableBean> getTableMap() {
        return dataBaseFactory.getTableBeanMap();
    }

    private JdbcTemplate getJdbcTemplate() {
        return dataBaseFactory.getJdbcTemplate();
    }

    private String getType() {
        return dataBaseFactory.getType();
    }

    /**
     * 查询
     * 
     * @param request
     * @return 记录集
     */
    @SuppressWarnings("deprecation")
    public List<Map<String, String>> select(CRUDRequest request) {
        TableBean tableBean = this.getTableMap().get(
                request.getTable().toUpperCase());

        // param
        Object[] param = this.getParam(request.getConditions());

        /********************
         * begin select sql
         ********************/
        // select
        StringBuffer sql = new StringBuffer(" select");

        // selectfields

        sql.append(" "
                + this.getSelectField(tableBean.getFields(),
                        request.getFields()));

        // from
        sql.append(" from");

        // table
        sql.append(" " + request.getTable());

        // wherefield
        sql.append(" "
                + this.getWhere(request.getConditions(), tableBean.getFields()));

        if (request.getPage() != null && request.getPage().getSize() != null) {
            if (request.getPage().getIndex() == null) {
                request.getPage().setIndex(1);
            }
            StringBuffer countSql = this.getCountSql(sql);
            request.getPage().setCount(
                    this.getJdbcTemplate().queryForInt(countSql.toString(),
                            param));
            sql = this.getPageSql(sql, request.getPage());

        }
        System.out.println(sql);
        /********************
         * end select sql
         ********************/
        return this.getJdbcTemplate().queryquery(sql.toString(),
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

    private StringBuffer getPageSql(StringBuffer sql, PageBean page) {
        int start = (page.getIndex() - 1) * page.getSize();
        int end = start + page.getSize();
        StringBuffer sqlBuffer = new StringBuffer();
        // oracle
        if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
            sqlBuffer.append("select * from (select rownum rn, a.* from (");
            sqlBuffer.append(sql);
            sqlBuffer.append(") a where rownum <=" + end + ")  where rn > "
                    + start);
        }
        // mysql
        else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
            sqlBuffer.append("select * from (");
            sqlBuffer.append(sql);
            sqlBuffer.append(") a  LIMIT " + start + "," + page.getSize());
        } else {
            sqlBuffer.append(sql);
        }
        return sqlBuffer;
    }

    private StringBuffer getCountSql(StringBuffer sql) {
        StringBuffer sqlBuffer = new StringBuffer(
                "select count(*) count from (");
        sqlBuffer.append(sql);
        sqlBuffer.append(") a");
        return sqlBuffer;
    }

    /**
     * 获取查询的字段
     * 
     * @param fieldMap
     * @param fields
     * @return
     */
    private String getSelectField(Map<String, FieldBean> fieldMap,
            String[] fields) {
        List<String> fieldList = new ArrayList<String>();
        if (ArrayUtils.isEmpty(fields)) {
            Iterator<Entry<String, FieldBean>> it = fieldMap.entrySet()
                    .iterator();
            while (it.hasNext()) {
                Entry<String, FieldBean> entry = it.next();
                fieldList.add(getDisplayFieldName(entry.getValue()));
            }
        } else {
            for (String field : fields) {
                if (fieldMap.containsKey(field)) {
                    fieldList.add(getDisplayFieldName(fieldMap.get(field)));
                } else {
                    fieldList.add(field);
                }
            }
        }

        return StringUtils.join(fieldList, ",");
    }

    /**
     * 获取查询参数值
     * 
     * @param conditions
     * @return
     */
    private Object[] getParam(List<ConditionBean> conditions) {

        List<String> params = new ArrayList<String>();
        List<String> treeParams = new ArrayList<String>();
        if (conditions != null) {
            for (ConditionBean condition : conditions) {
                if (condition.getValue() == null) {
                    continue;
                }
                String value;
                switch (ConditionEnum.getEnmu(condition.getCondition())) {
                    case IN:
                        value = condition.getValue();
                        String[] values = value.split(",");
                        for (String _value : values) {
                            params.add(_value);
                        }
                        break;

                    case TREEEQ:
                        value = condition.getValue();
                        treeParams.add(value);
                        break;
                    case TREEIN:
                        value = condition.getValue();
                        String[] tvalues = value.split(",");
                        for (String _value : tvalues) {
                            treeParams.add(_value);
                        }
                        break;
                    default:
                        params.add(condition.getValue());
                        break;
                }
            }
        }
        params.addAll(treeParams);
        return params.toArray();
    }

    /**
     * 获取条件字段
     * 
     * @param conditions
     * @param fields
     * @return
     */
    private String getWhere(List<ConditionBean> conditions,
            Map<String, FieldBean> fields) {
        List<String> sqlList = new ArrayList<String>();
        List<String> treeSqlList = new ArrayList<String>();

        if (conditions != null) {
            for (ConditionBean condition : conditions) {
                ConditionEnum ce = ConditionEnum.getEnmu(condition
                        .getCondition());

                String selectName = "";
                String selectValue = "";
                String selectRule = "";
                selectName = " "
                        + this.getWhereFieldName(ce,
                                fields.get(condition.getName()),
                                condition.getValue());

                switch (ce) {
                    case NOTEQ:
                        selectValue = " != ? ";
                        break;
                    case EQ:
                        selectValue = " = ? ";
                        break;
                    case GE:
                        selectValue = " >= ? ";
                        break;
                    case GT:
                        selectValue = " > ? ";
                        break;
                    case LE:
                        selectValue = " <= ? ";
                        break;
                    case LT:
                        selectValue = " < ? ";
                        break;
                    case LIKE:
                        // selectValue = " like '%?%' ";
                        selectValue = " like   '%'||?||'%'";
                        condition.setValue(condition.getValue());
                        break;
                    case LLIKE:
                        // selectValue = " like '?%' ";
                        selectValue = " like ? ";
                        condition.setValue(condition.getValue() + "%");
                        break;
                    case RLIKE:
                        // selectValue = " like '%?' ";
                        selectValue = " like ? ";
                        condition.setValue("%" + condition.getValue());
                        break;
                    case ISNULL:
                        selectValue = " is null ";
                        break;
                    case NOTNULL:
                        selectValue = " is not null ";
                        break;
                    case IN:

                        String[] values = condition.getValue().split(",");
                        int size = values.length;
                        List<String> _values = new ArrayList<String>();
                        for (int i = 0; i < size; i++) {
                            _values.add("?");
                        }
                        selectValue = " in (" + StringUtils.join(_values, ",")
                                + ") ";
                        break;
                    case TODAY:

                        // oracle
                        if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
                            selectValue = " = trunc(sysdate)";
                        }
                        // mysql
                        else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
                            selectValue = " = CURDATE()";
                        }

                        break;
                    case TREEEQ:
                        selectValue = " = ?";
                        selectRule = " connect by " + condition.getTreePid()
                                + " = prior " + condition.getTreeId();
                        break;
                    case TREEIN:
                        String[] tvalues = condition.getValue().split(",");
                        int tsize = tvalues.length;
                        List<String> _tvalues = new ArrayList<String>();
                        for (int i = 0; i < tsize; i++) {
                            _tvalues.add("?");
                        }
                        selectValue = " in (" + StringUtils.join(_tvalues, ",")
                                + ") ";
                        selectRule = " connect by " + condition.getTreePid()
                                + " = prior " + condition.getTreeId();
                        break;
                    default:
                        selectValue = " = ? ";
                        break;
                }

                if (ce.equals(ConditionEnum.TREEEQ)
                        || ce.equals(ConditionEnum.TREEIN)) {
                    treeSqlList.add(selectName + selectValue + selectRule);
                } else {
                    sqlList.add(selectName + selectValue + selectRule);
                }

            }

            if (sqlList.isEmpty()) {
                if (treeSqlList.isEmpty()) {
                    return "";
                } else {
                    return StringUtils.join(treeSqlList, " ");
                }

            } else {
                return " where " + StringUtils.join(sqlList, " and ")
                        + StringUtils.join(treeSqlList, " ");
            }

        } else {
            return "";
        }

    }

    /**
     * 新增
     * 
     * @param request
     * @return 记录集
     */
    public List<Map<String, String>> insert(CRUDRequest request) {
        Map<String, TableBean> tableMap = this.getTableMap();

        TableBean tableBean = tableMap.get(request.getTable().toUpperCase());
        List<LinkedHashMap<String, String>> datas = request.getDatas();
        List<String> keys = new ArrayList<String>();

        for (Map<String, String> data : datas) {

            if (tableBean.getSequenceMap() != null) {

                Iterator<Entry<String, SequenceInf>> it = tableBean
                        .getSequenceMap().entrySet().iterator();

                while (it.hasNext()) {
                    Entry<String, SequenceInf> _entry = it.next();
                    if (StringUtils.isEmpty(data.get(_entry.getKey()))) {
                        data.put(_entry.getKey(), _entry.getValue().nextval());
                    }

                }

            }

            if (tableBean.getKeyfield() != null) {
                keys.add(data.get(tableBean.getKeyfield().getName()));
            }

            // insert into table
            StringBuffer sql = new StringBuffer("insert into "
                    + request.getTable());

            // fields values fieldvalues
            sql.append(this.getInsertFieldandValue(data, tableBean.getFields()));
            this.getJdbcTemplate().execute(sql.toString());
        }

        // 获取返回的数据
        if (tableMap.get(request.getTable().toUpperCase()).getKeyfield() == null) {

            List<Map<String, String>> resultDatas = new ArrayList<Map<String, String>>();
            for (Map<String, String> data : datas) {
                resultDatas.add(data);
            }
            return resultDatas;
        } else {
            ConditionBean bean = new ConditionBean();
            bean.setCondition(ConditionEnum.IN.getValue());
            bean.setValue(StringUtils.join(keys, ","));
            bean.setName(tableMap.get(request.getTable().toUpperCase())
                    .getKeyfield().getName());
            request.addCondition(bean);
            return this.select(request);
        }

    }

    /**
     * 获取插入的字段以及值
     * 
     * @param data
     * @param fields
     * @return
     */
    private String getInsertFieldandValue(Map<String, String> data,
            Map<String, FieldBean> fields) {
        // datas
        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        List<String> fieldList = new ArrayList<String>();
        List<String> valueList = new ArrayList<String>();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            FieldBean fieldBean = fields.get(key);
            if (fieldBean != null) {
                fieldList.add(key);
                valueList.add(this.getRealFieldValue(fieldBean, value));
            }
        }

        return " (" + StringUtils.join(fieldList, ",") + ")" + " values "
                + " (" + StringUtils.join(valueList, ",") + ")";
    }

    /**
     * 删除执行
     * 
     * @param request
     * @return
     */
    public List<Map<String, String>> delete(CRUDRequest request) {
        TableBean tableBean = this.getTableMap().get(
                request.getTable().toUpperCase());
        List<Map<String, String>> list = this.select(request);

        // param
        Object[] param = this.getParam(request.getConditions());

        // delete
        StringBuffer sql = new StringBuffer(" delete " + request.getTable());

        // where
        sql.append(" "
                + this.getWhere(request.getConditions(), tableBean.getFields()));

        this.getJdbcTemplate().update(sql.toString(), param);
        return list;
    }

    public List<Map<String, String>> update(CRUDRequest request) {
        TableBean tableBean = this.getTableMap().get(
                request.getTable().toUpperCase());
        Map<String, String> data = request.getDatas().get(0);

        // update
        StringBuffer sql = new StringBuffer(" update " + request.getTable());

        // set
        sql.append(" set ");
        sql.append(this.getUpdateField(data, tableBean.getFields()));

        // where
        sql.append(" ");
        sql.append(this.getWhere(request.getConditions(), tableBean.getFields()));

        // param
        Object[] params = this.getParam(request.getConditions());

        this.getJdbcTemplate().update(sql.toString(), params);

        // trun select

        for (ConditionBean bean : request.getConditions()) {
            if (data.containsKey(bean.getName())) {
                bean.setValue(data.get(bean.getName()));
            }
        }

        return this.select(request);
    }

    public String getUpdateField(Map<String, String> data,
            Map<String, FieldBean> fields) {

        List<String> list = new ArrayList<String>();

        Iterator<Entry<String, String>> it = data.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            FieldBean fieldBean = fields.get(key);
            if (fieldBean != null) {
                list.add(" " + key + " = "
                        + this.getRealFieldValue(fieldBean, value));
            }
        }

        return StringUtils.join(list, ",");
    }

    /**
     * 获取显示字段的SQL语句
     * 
     * @param fieldBean
     * @return
     */
    private String getDisplayFieldName(FieldBean fieldBean) {
        DatetypeEnum de = DatetypeEnum.getEnmu(fieldBean.getType());
        switch (de) {
            case DATE:

                // oracle
                if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
                    return " to_char(" + fieldBean.getName() + ",'"
                            + DateUtil.ORACLE_PATTERN + "') as "
                            + fieldBean.getName();
                }
                // mysql
                else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
                    return " DATE_FORMAT(" + fieldBean.getName() + ",'"
                            + DateUtil.MYSQL__PATTERN + "') as "
                            + fieldBean.getName();
                }

            default:
                return fieldBean.getName();
        }

    }

    /**
     * 获取条件显示字段
     * 
     * @param fieldBean
     * @param value
     * @return
     */
    private String getWhereFieldName(ConditionEnum ce, FieldBean fieldBean,
            String value) {

        switch (ce) {
            case TODAY:
                // oracle
                if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
                    return " trunc(" + fieldBean.getName() + ")";
                }
                // mysql
                else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
                    return "DATE(" + fieldBean.getName() + ")";
                }
            case TREEEQ:
                return "start with " + fieldBean.getName();
            case TREEIN:
                return "start with " + fieldBean.getName();
            default:
                DatetypeEnum de = null;
                try {
                    de = DatetypeEnum.getEnmu(fieldBean.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (de) {
                    case DATE:
                        // oracle
                        if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
                            return " to_char(" + fieldBean.getName() + ",'"
                                    + DateUtil.getOraclePattern(value) + "')";
                        }
                        // mysql
                        else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
                            return " DATE_FORMAT(" + fieldBean.getName() + ",'"
                                    + DateUtil.getMySqlPattern(value) + "')";
                        }

                    default:
                        return fieldBean.getName();
                }
        }
    }

    /**
     * 获取真实的值
     * 
     * @param fieldBean
     * @param value
     * @return
     */
    public String getRealFieldValue(FieldBean fieldBean, String value) {
        switch (DatetypeEnum.getEnmu(fieldBean.getType())) {
            case DATE:
                // oracle
                if (this.getType().equals(DataBaseUtil.TYPE_ORACLE)) {
                    return "to_date('" + value + "','"
                            + DateUtil.getOraclePattern(value) + "')";
                }
                // mysql
                else if (this.getType().equals(DataBaseUtil.TYPE_MYSQL)) {
                    return "STR_TO_DATE('" + value + "','"
                            + DateUtil.getMySqlPattern(value) + "')";

                }
            default:
                return "'" + value + "'";
        }
    }
}
