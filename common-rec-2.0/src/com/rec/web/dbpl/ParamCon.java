package com.rec.web.dbpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ���������࣬����ִ��������ʱjavabean��Ӧ����������<br>
 * �����趨����ֵ��ͨ����ʵ�������ݿ��в�������
 * 
 * @author Recolar
 * @see
 * @version 1.0.0 2007-2-18
 * @since JDK1.5
 * 
 */
public class ParamCon {

    /**
     * sql���,���Գ���ռλ��
     */
    private String sql;

    /**
     * Vo�������
     */
    private Object classObj;

    /**
     * Vo�����������
     */
    private Object[] objs;

    /**
     * ���ڱ���ParamObjectʵ��
     */
    private List paramList = new ArrayList();

    /**
     * ���ñ�����,һ����putFieldValue�������ʹ��
     */
    private String tableName;

    /**
     * Key:�ֶ�����,Value:�����ֵ,��ӳΪObject����
     */
    private Map fieldObjMap = new LinkedHashMap();

    /**
     * ����FieldObjMap,�����������ݲ������ݿ�
     */
    private List batchFieldObjList = new ArrayList();

    /**
     * ��ȡ�����õı�����
     * 
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * ���ñ�����
     * 
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * ��ӱ���ֶ����ƺ��ֶ�ֵ
     * 
     * @param fieldName �ֶ�����
     * @param fieldValue �ֶ�ֵ
     */
    public void putFieldValue(String fieldName, Object fieldValue) {
        this.fieldObjMap.put(fieldName, fieldValue);
    }

    /**
     * ��ӶԲ������ݵ�����
     */
    public void addFieldValueBatch() {
        if (this.fieldObjMap != null && !this.fieldObjMap.isEmpty()) {
            this.batchFieldObjList.add(this.fieldObjMap);
            this.fieldObjMap = new LinkedHashMap();
        }
    }

    /**
     * ��ȡ������������б�,List�а������Map
     * 
     * @return List
     */
    public List getBatchFieldObjList() {
        return this.batchFieldObjList;
    }

    /**
     * ��ո�ʵ���еĸ�������ֵ
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
     * ����������ʱ��Ҫ���õ��Ĳ���
     * 
     * @param index �±�,��ӦInsert����?�±�λ��,��1��ʼ
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
     * ����sql����Vo�������
     * 
     * @param sql SQL���
     * @param classObj ��һ��Vo����
     * @return ParamCon
     */
    public ParamCon configParamCon(String sql, Object classObj) {
        this.sql = sql;
        this.classObj = classObj;
        return this;
    }

    /**
     * ����sql����Vo�����������,��Ҫ����������
     * 
     * @param sql SQL���
     * @param classObj ��һ��Vo��������
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
     * @return int paramList������ParamObjectʵ���ĳ���
     */
    public int getParamLength() {
        return this.paramList.size();
    }

    /**
     * ����������ParamList�л�ȡParamObjectʵ��
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
     * �Ƿ�����ִ��
     * 
     * @return true:��
     */
    public boolean isBatchExecute() {
        return (this.objs != null && this.objs.length > 1);
    }

}
