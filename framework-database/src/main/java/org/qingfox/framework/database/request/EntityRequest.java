package org.qingfox.framework.database.request;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qingfox.framework.database.request.base.BaseRequest;
import org.qingfox.framework.database.stereotypes.Column;
import org.qingfox.framework.database.utils.EntityUtil;

import org.qingfox.framework.common.utils.ReflectUtil;

public class EntityRequest<T> extends BaseRequest<T> {

    private static final long serialVersionUID = 8322405977316740094L;

    private T entity;
    private List<T> datas;
    private Class<T> entityClass;
    private Map<String, String> keyMapEntity;

    public EntityRequest(Class<T> entityClass) {
        this.entityClass = entityClass;
        keyMapEntity = new HashMap<String, String>();
        String table = EntityUtil.getTable(entityClass);
        Field[] fields = ReflectUtil.getFields(entityClass);
        for (Field field : fields) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                keyMapEntity.put(column.value().toUpperCase(), field.getName());
                this.addField(column.value());
            }
        }
        this.setTable(table);
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public void addData(T data) {
        if (this.datas == null) {
            this.datas = new ArrayList<T>();
        }
        this.datas.add(data);
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public Map<String, String> getKeyMapEntity() {
        return keyMapEntity;
    }

    public void setKeyMapEntity(Map<String, String> keyMapEntity) {
        this.keyMapEntity = keyMapEntity;
    }

}
