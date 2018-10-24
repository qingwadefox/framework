\package com.framework.plugin.web.common.server.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.framework.plugin.web.common.bean.ConditionBean;
import com.framework.plugin.web.common.web.spring.factory.DataBaseFactory;
import com.framework.common.enums.ConditionEnum;
import com.framework.common.utils.ReflectUtil;

@Repository
public class EntityDao {

    @Autowired
    private DataBaseFactory dataBaseFactory;

    private SessionFactory getSessionFactory() {
        return dataBaseFactory.getSessionFactory();
    }

    /**
     * 根据ID获取数据对象
     * 
     * @param id
     * @return
     */
    public Object get(Class<?> entityClass, Object id) {
        return this.getCurrentSession().get(entityClass, (Serializable) id);
    }

    public Object update(Object entity) throws IllegalArgumentException,
            SecurityException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException,
            NoSuchFieldException {

        String idName = this.getSessionFactory()
                .getClassMetadata(entity.getClass())
                .getIdentifierPropertyName();

        Object _entity = this.get(entity.getClass(),
                (Serializable) ReflectUtil.getFieldValue(entity, idName));
        for (Field field : entity.getClass().getDeclaredFields()) {
            try {
                Object value = ReflectUtil.getFieldValue(entity, field);
                if (value != null) {
                    ReflectUtil.setFieldValue(_entity, field, value);
                }
            } catch (Exception e) {
                continue;
            }
        }
        this.getCurrentSession().saveOrUpdate(_entity);
        return entity;
    }

    /**
     * 插入单个数据对象
     * 
     * @param entity
     * @return
     */
    public Object insert(Object entity) {
        return this.get(entity.getClass(), getCurrentSession().save(entity));
    }

    public void delete(Object entity) {
        this.getCurrentSession().delete(entity);
    }

    /**
     * 根据数据对象条件查询多个数据对象
     * 
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws SecurityException
     */

    @SuppressWarnings("unchecked")
    public List<Object> select(List<ConditionBean> conditionList,
            Class<?> entityClass) throws IllegalArgumentException,
            IllegalAccessException, SecurityException,
            InvocationTargetException, NoSuchMethodException {
        Session session = this.getCurrentSession();
        Criteria criteria = session.createCriteria(entityClass);

        if (conditionList != null) {
            for (ConditionBean bean : conditionList) {
                this.addCriteria(criteria, bean.getName(),
                        bean.getEntityValue(),
                        ConditionEnum.getEnmu(bean.getCondition()));
            }
        }

        List<Object> list = criteria.list();
        return list;
    }

    private Session getCurrentSession() {
        return this.getSessionFactory().getCurrentSession();
    }

    private void addCriteria(Criteria criteria, String name, Object value,
            ConditionEnum condition) {
        switch (condition) {
            case NOTEQ:
                criteria.add(Restrictions.ne(name, value));
                break;
            case EQ:
                criteria.add(Restrictions.eq(name, value));
                break;
            case GE:
                criteria.add(Restrictions.ge(name, value));
                break;
            case GT:
                criteria.add(Restrictions.gt(name, value));
                break;
            case LE:
                criteria.add(Restrictions.le(name, value));
                break;
            case LT:
                criteria.add(Restrictions.lt(name, value));
                break;
            case LIKE:
                criteria.add(Restrictions.like(name, value.toString(),
                        MatchMode.ANYWHERE));
                break;
            case LLIKE:
                criteria.add(Restrictions.like(name, value.toString(),
                        MatchMode.START));
                break;
            case RLIKE:
                criteria.add(Restrictions.like(name, value.toString(),
                        MatchMode.END));
                break;
            case ISNULL:
                criteria.add(Restrictions.isNull(name));
                break;
            case NOTNULL:
                criteria.add(Restrictions.isNotNull(name));
                break;
            default:
                criteria.add(Restrictions.eq(name, value));
                break;
        }

    }

}
