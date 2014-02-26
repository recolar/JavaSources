package com.rec.web.dbpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数设置类，用于执行批处理时javabean对应的数据类型<br>
 * 或者设定参数值，通过本实例向数据库中插入数据
 * 
 * @author Recolar
 * @see
 * @version 1.0.0 2007-2-18
 * @since JDK1.5
 * 
 */
public class ParamCon {

    /**
     * sql语句,可以出现占位符
     */
    private String sql;

    /**
     * Vo对象参数
     */
    private Object classObj;

    /**
     * Vo对象数组参数
     */
    private Object[] objs;

    /**
     * 用于保存ParamObject实例
     */
    private List paramList = new ArrayList();

    /**
     * 设置表名称,一般与putFieldValue方法配合使用
     */
    private String tableName;

    /**
     * Key:字段名称,Value:插入的值,反映为Object类型
     */
    private Map fieldObjMap = new LinkedHashMap();

    /**
     * 保存FieldObjMap,用于批量数据插入数据库
     */
    private List batchFieldObjList = new ArrayList();

    /**
     * 获取所设置的表名称
     * 
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 设置表名称
     * 
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 添加表的字段名称和字段值
     * 
     * @param fieldName 字段名称
     * @param fieldValue 字段值
     */
    public void putFieldValue(String fieldName, Object fieldValue) {
        this.fieldObjMap.put(fieldName, fieldValue);
    }

    /**
     * 添加对插入数据的批量
     */
    public void addFieldValueBatch() {
        if (this.fieldObjMap != null && !this.fieldObjMap.isEmpty()) {
            this.batchFieldObjList.add(this.fieldObjMap);
            this.fieldObjMap = new LinkedHashMap();
        }
    }

    /**
     * 获取批量处理参数列表,List中包含多个Map
     * 
     * @return List
     */
    public List getBatchFieldObjList() {
        return this.batchFieldObjList;
    }

    /**
     * 清空该实例中的各个属性值
     */
    public void clear() {
        this.sql = "";
        this.fieldObjMap.clear();
        this.batchFieldObjList.clear();
        this.objs = null;
        this.paramList.clear();
        this.tableName = "";
        this.classObj = null;
    }

    /**
     * 设置批处理时需要调用到的参数
     * 
     * @param index 下标,对应Insert语句的?下标位置,从1开始
     * @param typeName
     * @param paramType
     */
    public void setParam(int index, String typeName, int ParamTypes) {
        ParamObject po = new ParamObject();
        po.setIndex(index);
        po.setTypeName(typeName);
        po.setParamType(ParamTypes);
        paramList.add(po);
    }

    /**
     * 设置sql语句和Vo对象参数
     * 
     * @param sql SQL语句
     * @param classObj 是一个Vo对象
     * @return ParamCon
     */
    public ParamCon configParamCon(String sql, Object classObj) {
        this.sql = sql;
        this.classObj = classObj;
        return this;
    }

    /**
     * 设置sql语句和Vo对象数组参数,主要用于批处理
     * 
     * @param sql SQL语句
     * @param classObj 是一个Vo对象数组
     * @return ParamCon
     */
    public ParamCon configParamCon(String sql, Object[] objs) {
        this.sql = sql;
        this.objs = objs;
        return this;
    }

    /**
     * 
     * @return List paramList
     */
    public List getParamList() {
        return this.paramList;
    }

    /**
     * 
     * @return int paramList所保存ParamObject实例的长度
     */
    public int getParamLength() {
        return this.paramList.size();
    }

    /**
     * 根据索引在ParamList中获取ParamObject实例
     * 
     * @param index
     * @return
     */
    public ParamObject getParamObject(int index) {
        return (ParamObject) paramList.get(index);
    }

    public Object getClassObj() {
        return classObj;
    }

    public void setClassObj(Object classObj) {
        this.classObj = classObj;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getObjs() {
        return objs;
    }

    public void setObjs(Object[] objs) {
        this.objs = objs;
    }

    /**
     * 是否批量执行
     * 
     * @return true:是
     */
    public boolean isBatchExecute() {
        return (this.objs != null && this.objs.length > 1);
    }

}
