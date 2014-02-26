package com.rec.web.dbutil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rec.exception.RecRunTimeException;
import com.rec.service.RecDate;
import com.rec.service.StringUtil;
import com.rec.service.SysUtil;
import com.rec.sys.CacheManager;
import com.rec.sys.impl.GlobalCache;
import com.rec.web.dbpl.ParamCon;
import com.rec.web.dbpl.ParamObject;
import com.rec.web.dbpl.ParamTypes;

/**
 * @author Recolar
 * @version 1.0.1 2007-1-6
 * @since JDK1.5.0
 */
public class DBSessionImpl implements DBSession {

    private static final Log log = LogFactory.getLog(DBSessionImpl.class);

    private Connection conn = null;

    DBSessionImpl() {
    }

    /**
     * �ṩһ��Connection������DBSessionʵ��<br>
     * Connection���������Զ��ύ
     * 
     * @param conn Connection
     */
    DBSessionImpl(Connection conn) {
        this.conn = conn;
        // try {
        // if (this.conn.getAutoCommit()) {
        // this.conn.setAutoCommit(false);
        // if (log.isDebugEnabled()) {
        // log.debug("��DBSession�ڲ�������֮Connection�����Զ��ύ��������Ϊfalse");
        // }
        // }
        // } catch (SQLException e) {
        // throw new RecRunTimeException(e);
        // }
    }

    /**
     * ִ��SQL���,��������Ӱ�������<br>
     * 
     * @param sql SQL��ɾ�����
     * @return int �������Ϊ-1���ʾ����ʱ�����쳣
     */
    public int executeSQL(String sql) {
        PreparedStatement ppst = null;
        int effectRow = 0;
        try {
            ppst = conn.prepareStatement(sql);
            effectRow = ppst.executeUpdate();
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
                log.info("����Ӱ������:" + effectRow);
            }
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException("ִ��SQLʧ��...", e);
        } finally {// �Ӿ������ر����ݿ�������Դ�Ĳ���
            ConnUtil.releaseConnResource(ppst);
        }
        return effectRow;// ������Ӱ������
    }

    /**
     * �ڲ������쳣ʱ������ع�..
     */
    private void rollBack() {
        try {
            conn.rollback();
            if (log.isErrorEnabled()) {
                log.error("�����쳣,���񽫻ع�!!!");
            }
        } catch (Exception e2) {
        }
    }

    /**
     * ����Ƿ����ĳһ����¼
     * 
     * @param sql ��ѯSQL
     * @return true:���� false:������
     */
    public boolean getExistRecord(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        boolean isExist = false;
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (rs.next()) {
                isExist = true;
            }
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
                log.info("�Ƿ����ĳ�м�¼:" + isExist);
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return isExist;
    }

    /**
     * ����һ�м�¼�����ݿ���ȥ������obj������Ҫ�����¼��JavaBean����
     * 
     * @param sql �������ݿ��SQL���<br>
     *            select��䣬��֧���ֶα���,���������Ʊ����JavaBean����������һ��<br>
     * 
     * @param obj JavaBean����,��װ�˶�һ�����ݱ��getter��setter����
     * @return ����ɹ�����true���򷵻�false
     */
    public boolean insertBeanDataIntoDB(String sql, Object obj) {
        boolean isOk = false;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("��������:" + obj.getClass().getName());
            }
            ppst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (log.isInfoEnabled())
                log.info("ִ��SQL:" + sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if (log.isDebugEnabled())
                log.debug("��ȡ���ݿ�Ԫ������Ϣ(ResultSetMetaData),���ڽ������е��������Ͳ����÷����������");
            rs.moveToInsertRow();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String fieldName = rsmd.getColumnName(i);
                setDataToResultSet(rs, fieldName, obj);
            }
            rs.insertRow();// ִ�в����¼�Ĳ���
            if (log.isDebugEnabled())
                log.debug("�����������ɹ�...(��Ҫ�ύ����)");
            isOk = true;
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return isOk;
    }

    /**
     * ������JavaBean��������,֧��insert��update���,<br>
     * sql�﷨��ʽΪinsert into table(aa,nn) values(?,?),����<br>
     * update table set aa=?,bb=? where id=?
     * 
     * @param sql Sql���
     * @param obj JavaBean����
     * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
     * @return true:ִ�гɹ�
     */
    public boolean insertOrUpdateBeanDataIntoDB(String sql, Object obj, ParamCon pc) {
        boolean isOk = false;
        PreparedStatement ppst = null;
        try {
            ppst = conn.prepareStatement(sql);
            if (log.isDebugEnabled()) {
                log.debug("����ParamCon����...");
                log.debug("���ṩ��Pojoʵ��:" + obj.getClass().getName());
                for (Iterator i = pc.getParamList().iterator(); i.hasNext();) {
                    ParamObject paramObj = (ParamObject) i.next();
                    log.debug("��������:" + paramObj.getIndex() + "-->" + paramObj.getTypeName().toUpperCase());
                }
            }
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            boolean isProOk = setPreparedStatementParam(ppst, obj, pc, false);
            if (!isProOk) {
                throw new RecRunTimeException("���������������Ƿ���ȷ����ParamCon�����Ƿ�������ȷ");
            }
            ppst.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("�������ݳɹ�(��Ҫ�ύDBSession����)!" + SysUtil.objectToString(obj, true, "\t"));
            }
            isOk = true;
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(ppst);
        }
        return isOk;
    }

    /**
     * ������JavaBean��������,֧��insert��update���,<br>
     * sql�﷨��ʽΪinsert into table(aa,nn) values(?,?),����<br>
     * update table set aa=?,bb=? where id=?
     * 
     * @param pc ParamCon����
     * @return true:ִ�гɹ�
     */
    public boolean insertOrUpdateBeanDataIntoDB(ParamCon pc) {
        if (log.isDebugEnabled()) {
            log.debug("��ParamConʵ���л�ȡ�����õ�sql�Լ�JavaBean�������");
        }
        String sql = pc.getSql();
        Object classObj = pc.getClassObj();
        return insertOrUpdateBeanDataIntoDB(sql, classObj, pc);
    }

    /**
     * �����ݲ��뵽���ݿ���,֧����������<br>
     * ��ParamCon��putFieldValue���ò�����ֶ������Լ�����ֵ,������֮����Ҫ����addFieldValueBatch()����
     * 
     * @param pc ParamCon ���ݿ���ݷ�װ����
     * @return boolean ����ɹ�����true����֮����false
     */
    public boolean insertDataIntoDB(ParamCon pc) {
        PreparedStatement ppst = null;
        boolean succeed = false;
        try {
            String sql = buildInsertSql(pc);
            ppst = conn.prepareStatement(sql);
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
                log.info("ʹ���˲����󶨶��󴫵ݲ������뵽���ݿ���");
            }
            for (Iterator i = pc.getBatchFieldObjList().iterator(); i.hasNext();) {
                Map map = (Map) i.next();
                int index = 1;
                for (Iterator i2 = map.entrySet().iterator(); i2.hasNext();) {
                    Entry entry = (Entry) i2.next();
                    Object fieldValue = entry.getValue();
                    ppst.setObject(index++, fieldValue);
                    if (log.isDebugEnabled()) {
                        log.debug("�ֶ�����:" + entry.getKey() + ",�ֶ�ֵ:" + fieldValue);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("::::::::::::::�����������...");
                }
                ppst.addBatch();
            }
            ppst.executeBatch();
            if (log.isDebugEnabled()) {
                log.debug("�������ݳɹ�(��Ҫ�ύDBSession����)!");
            }
            succeed = true;
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(ppst);
        }
        return succeed;
    }

    private String buildInsertSql(ParamCon pc) {
        String insertSql = "INSERT INTO {0}({1}) VALUES({2})";
        String tableName = pc.getTableName();
        StringBuffer fieldName = new StringBuffer();
        StringBuffer flag = new StringBuffer();
        Map map = (Map) pc.getBatchFieldObjList().get(0);
        Set keySet = map.keySet();
        for (Iterator i = keySet.iterator(); i.hasNext();) {
            fieldName.append(String.valueOf(i.next()));
            flag.append("?");
            if (i.hasNext()) {
                fieldName.append(",");
                flag.append(",");
            }
        }
        return MessageFormat.format(insertSql,
                new Object[] { tableName, fieldName.toString().toUpperCase(), flag.toString() });
    }

    /**
     * ��ȡ������Ϣ,��Sql�������Ѿ�д���˷�����Ϣ��ȡ���߼�<br>
     * ������Ի�ȡsql��ѯ����еĽ������<br>
     * ע�����sql���������ý��ֻ����һ��һ��
     * 
     * @param sql ��ѯ���
     * @return String ������.�����������ֵĻ�Ҫ����ת��
     */
    public String getGroupValueBySql(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        String value = "";
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (rs.next()) {
                value = rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return value;
    }

    /**
     * ��ȡ������Ϣ.������ĳ��table��Ҫ��ȡĳ�е�max,min,avg,sum,count�ȵ���Ϣ<br>
     * ���ص����ַ�������,ʵ��ʹ����Ҫ�ǵ�����ת��.<br>
     * ע:ֻ�ܻ�ȡһ��һ�еķ�����Ϣ
     * 
     * @param tableName ������
     * @param colName ��Ҫ��ȡ������Ϣ����
     * @param groupFlag ���鶯����־
     * @return ������ �����쳣����""
     */
    public String getGroupValue(String tableName, String colName, String groupFlag) {
        String sql = "Select " + groupFlag + " value From " + tableName;
        sql = MessageFormat.format(sql, new Object[] { colName });
        PreparedStatement ppst = null;
        ResultSet rs = null;
        String value = "";
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            if (rs.next()) {
                value = rs.getString("value");
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return value;
    }

    /**
     * ��ȡ������Ϣ.������ĳ��table��Ҫ��ȡĳ�е�max,min,avg,sum,count�ȵ���Ϣ<br>
     * ���ص����ַ�������,ʵ��ʹ����Ҫ�ǵ�����ת��.<br>
     * ע:ֻ�ܻ�ȡһ��һ�еķ�����Ϣ
     * 
     * @param tableName ������
     * @param filterSql ����SQL�ַ���(Where�־�),��:Where Typeid=0 group by EmpNo
     * @param colName ��Ҫ��ȡ������Ϣ����
     * @param groupFlag ���鶯����־
     * @return ������
     */
    public String getGroupValue(String tableName, String filterSql, String colName, String groupFlag) {
        String sql = "Select " + groupFlag + " value From " + tableName + " " + filterSql;
        sql = MessageFormat.format(sql, new Object[] { colName });
        PreparedStatement ppst = null;
        ResultSet rs = null;
        String value = "";
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            if (rs.next()) {
                value = rs.getString("value");
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return value;
    }

    /**
     * ���ݱ�����ȡ�ñ������
     * 
     * @return int ����
     */
    public int getTableRows(String tableName) {
        String sql = "Select Count(*) Num From " + tableName;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            if (rs.next()) {
                return rs.getInt("Num");
            }
        } catch (SQLException e) {
            throw new RecRunTimeException("��ȡ�ñ������ʧ��......", e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return 0;

    }

    /**
     * ִ��������ķ�������������Ӱ�������
     * 
     * @param batchSql ����ִ���������sql����ַ�������
     * @return ��������Ӱ�������������쳣�򷵻�-1
     * @throws Exception
     */
    public int executeBatchSql(String[] batchSql) {
        Statement ste = null;
        int effectRow = 0;
        try {
            ste = conn.createStatement();
            for (int i = 0; i < batchSql.length; i++) {
                ste.addBatch(batchSql[i]);// ��ӵ�St��
            }
            if (log.isDebugEnabled()) {
                log.debug("ִ��������...");
            }
            for (int i = 0; i < batchSql.length; i++) {
                if (log.isInfoEnabled()) {
                    log.info("ִ��SQL(������):" + batchSql[i]);
                }
            }
            int[] nums = ste.executeBatch();// ִ��������

            // ͳ��������Ӱ�������
            for (int i = 0; i < nums.length; i++) {
                effectRow += nums[i];
            }
            if (log.isDebugEnabled()) {
                log.debug("�ύ������...");
                log.debug("������������Ӱ������:" + effectRow);
            }
        } catch (Exception e) {
            effectRow = -1;
            rollBack();
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(ste);
        }
        return effectRow;
    }

    /**
     * ִ��������SQL�ķ���
     * 
     * @param sql ע��:����ΪSelect���<br>
     *            ����Ҫ�����Users����ΪID,USERNAME,AGE,�����д��select ID,USERNAME,AGE From
     *            users
     * @param objs ��ӦҪ������JavaBean����(Vo����)
     * @return �������ݿ��¼������,����-1���ʾʧ��
     */
    public int executeBatchSql(String sql, Object[] objs) {
        int effectRow = -1;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);// �ɸ��½����
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
                log.info("�����ݽ���������������ݿ���...");
            }
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// ��ȡ��ѯSQL�ĸ���������
            for (int i = 0; i < objs.length; i++) {
                rs.moveToInsertRow();
                for (int j = 1; j < rsmd.getColumnCount() + 1; j++) {
                    setDataToResultSet(rs, rsmd.getColumnName(j), objs[i]);
                }
                if (log.isDebugEnabled()) {
                    log.debug("��������[" + (i + 1) + "]:" + objs[i].getClass().getName());
                }
                rs.insertRow();
            }
            effectRow = objs.length;
            if (log.isDebugEnabled()) {
                log.debug("������ִ�гɹ�(��Ҫ�ύDBSession����)!���������ݱ��м�¼��:" + objs.length);
            }
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return effectRow;
    }

    /**
     * ִ�����������,ֻ֧��insert���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
     * 
     * @param sql Sql���
     * @param objs JavaBean��������
     * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
     * @return ���سɹ��������ݿ������
     */
    public int executeBatchSql(String sql, Object[] objs, ParamCon pc) {
        int effectRow = -1;
        PreparedStatement ppst = null;
        try {
            ppst = conn.prepareStatement(sql);
            if (log.isDebugEnabled()) {
                log.debug("����ParamCon����...");
                log.debug("���ṩ��Pojoʵ��:" + objs[0].getClass().getName());
                for (Iterator i = pc.getParamList().iterator(); i.hasNext();) {
                    ParamObject paramObj = (ParamObject) i.next();
                    log.debug("��������:" + paramObj.getIndex() + "-->" + paramObj.getTypeName().toUpperCase());
                }
            }
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            for (int i = 0; i < objs.length; i++) {
                boolean isProOk = setPreparedStatementParam(ppst, objs[i], pc, true);
                if (!isProOk) {
                    throw new RecRunTimeException("��ӵ�������������������������Ƿ���ȷ����ParamCon�����Ƿ�������ȷ");
                }
            }
            ppst.executeBatch();
            if (log.isDebugEnabled()) {
                log.debug("������ִ�гɹ�(��Ҫ�ύDBSession����)...");
                log.debug("�����������������:" + objs.length + "��");
            }
            effectRow = objs.length;
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(ppst);
        }
        return effectRow;
    }

    /**
     * ִ�����������,ֻ֧��insert���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
     * 
     * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
     * @return ���سɹ��������ݿ������
     */
    public int executeBatchSql(ParamCon pc) {
        String sql = pc.getSql();
        Object[] objs = pc.getObjs();
        return executeBatchSql(sql, objs, pc);
    }

    /**
     * ��ȡ��ѯ���Map��Map�����Key�����ֶ����ƣ�Value�����ֶ�ֵ<br>
     * ֻ��Select��ѯ�����Ч,ע��Map��key�����ֶ�����ʱ�ǵ�Ҫ�Ͳ�ѯ����е��ֶ�������һ��<br>
     * ��Ҫ�ǲ�ѯĳһ�е����ݡ������û���¼��֤�����sql���ض�����Ϣ��ֻ�����һ����Ϣ<br>
     * ������ص�map.isEmpty()==true���ѯ��������
     * 
     * @param sql ��ѯSQL���
     * @return Map ����,��װ�˲�ѯ���
     */
    public Map getQueryMap(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// ��ȡ���ݼ�Ԫ������Ϣ
            int count = rsmd.getColumnCount();// �ֶ�����
            String[] fieldList = new String[count];
            for (int i = 1; i < count + 1; i++) {
                fieldList[i - 1] = rsmd.getColumnName(i);// �Ѳ�ѯSql��������漰�����ֶ���䵽List������
            }
            if (rs.next()) {// �������ݼ�
                for (int i = 0; i < fieldList.length; i++) {// ����ĳһ�������ֶ�
                    String value = rs.getString(fieldList[i]);
                    value = value == null ? "" : value;
                    map.put(fieldList[i], value);
                }
            }
        } catch (Exception e) {
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return map;
    }

    /**
     * ��ȡ��ѯ�����б���Ϣ��List����Map����,Map�����Key�����ֶ����ƣ�Value�����ֶ�ֵ<br>
     * ֻ��Select��ѯ�����Ч,ע��Map��key�����ֶ�����ʱ�ǵ�Ҫ�Ͳ�ѯ����е��ֶ�������һ��
     * 
     * @param sql ��ѯSQL���
     * @return List ����
     */
    public List getQueryListMap(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        List qryResultList = new ArrayList();
        try {
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// ��ȡ���ݼ�Ԫ������Ϣ
            if (log.isDebugEnabled())
                log.debug("��ȡ���ݼ�Ԫ������Ϣ(ResultSetMetaDataʵ��),�Ա��һ����ȡ�����ֶ���Ϣ-->" + rsmd.toString());
            int count = rsmd.getColumnCount();// �ֶ�����
            String[] fieldList = new String[count];
            for (int i = 1; i < count + 1; i++) {
                fieldList[i - 1] = rsmd.getColumnName(i);// �Ѳ�ѯSql��������漰�����ֶ���䵽List������
            }
            if (log.isDebugEnabled()) {
                log.debug("�����ֶ���Ϣ:" + Arrays.asList(fieldList).toString());
            }
            while (rs.next()) {// �������ݼ�
                Map map = new HashMap();
                for (int i = 0; i < fieldList.length; i++) {// ����ĳһ�������ֶ�
                    Object value = rs.getObject(fieldList[i]);
                    value = (value == null ? "" : value);
                    map.put(fieldList[i], value);
                }
                qryResultList.add(map);
            }
        } catch (Exception e) {
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return qryResultList;
    }

    /**
     * ���ݲ�ѯSQL������ý����װ��һ��JavaVo��������ȥ�����ش˶���<br>
     * ֻȡһ�����ݡ����sql���ض���������ȡ��һ��
     * 
     * @param sql ��ѯ���
     * @param cla Pojo��Class����
     * @return Object һ��Vo����,�������Ϊnull��ò�ѯ���û�в�ѯ���ý��
     */
    public Object getQueryObj(String sql, Class cla) {
        return getQueryData(sql, cla, false);
    }

    /**
     * ����SQL��ѯ����ȡ����Vo��List
     * 
     * @param sql ��ѯ���
     * @param cla Pojo��Class����
     * @return List ���϶���,List�б������Cla�����ʵ��
     */
    public List getQueryObjList(String sql, Class cla) {
        return (List) getQueryData(sql, cla, true);
    }

    /**
     * getQueryObj��getQueryObjList���õ�ȡ������<br>
     * �߼������Ϊ��ߴ���ʹ����.������÷���
     * 
     * @param sql ��ѯ���
     * @param cla Pojo��Class����
     * @param isQueryList
     *            true:��getQueryObjList��������,����List,false:��getQueryObj��������,
     *            ����Object
     * @return Object getQueryObjList��������,����List,���ΪgetQueryObj��������,����Object
     */
    private Object getQueryData(String sql, Class cla, boolean isQueryList) {
        List resultList = new ArrayList();
        Object resultObj = null;
        GlobalCache cache = CacheManager.getGlobalCache();
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql);
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();// ��ѯ���õ�������
            // �����ѯ�����ֶ�
            String[] colStr = new String[count];
            for (int i = 1; i <= count; i++) {
                colStr[i - 1] = rsmd.getColumnName(i);
            }
            // ���ṩ��POJO�����·��
            String cacheKey = QUERY_CACHEKEY_LCL + cla.getName();
            // ���������û�л���Bean������Ϣ
            if (!cache.contains(cacheKey)) {
                if (log.isDebugEnabled()) {
                    log.debug("û�ж�" + cla.getName() + "���з��仺��!");
                }
                cacheFiller(cla);// ���÷������ж�bean���仺�������Ϣ
            }
            if (cache.contains(cacheKey)) {
                if (log.isDebugEnabled()) {
                    log.debug("�Ѿ��ɹ���" + cla.getName() + "���з��仺��");
                    log.debug("�ִӻ����л�ȡ" + cla.getName() + "�ĸ����ֶ���Ϣ!!!");
                    Map beanCacheMap = (Map) cache.getProperty(cacheKey);
                    for (int i = 0; i < colStr.length; i++) {
                        CacheParam cp = (CacheParam) beanCacheMap.get(colStr[i]);
                        log.debug("��ѯ�ֶ�:" + colStr[i] + "ӳ��Ϊ---->" + cla.getName() + ":" + cp.getFieldName() + "("
                                + cp.getParamClassName() + ")");
                    }
                }
            } else {
                throw new RecRunTimeException("���ڶ�" + cla.getName() + "����ʧ��,��̨����������в�ѯ!");
            }
            Map beanCacheMap = (Map) cache.getProperty(cacheKey);
            while (rs.next()) {
                resultObj = cla.newInstance();
                for (int i = 0; i < colStr.length; i++) {
                    String colName = colStr[i];
                    CacheParam cp = (CacheParam) beanCacheMap.get(colName);
                    Method met = cp.getSetterMethodObj();
                    met.invoke(resultObj,
                            new Object[] { getReturnObjByQueryObjList(rs, cp.getFieldName(), cp.getParamClassName()) });
                }
                if (isQueryList) {
                    resultList.add(resultObj);
                } else {
                    break;
                }
            }
        } catch (Exception err) {
            throw new RecRunTimeException(err);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        if (log.isDebugEnabled()) {
            log.debug("��ѯ������������::" + resultList.size());
        }
        return isQueryList ? resultList : resultObj;
    }

    /**
     * ��SQL��ѯ���÷�װ��һ������������<br>
     * ע��:��SQL��ѯ�����������¼������������װ�˴˼�¼<br>
     * �����SQL��ѯ���ص��Ƕ��м�¼������������װ�˵�һ�м�¼<br>
     * �����ѯ����û�м�¼���򷵻�null
     * 
     * @param sql SQL�������
     * @return Object[]
     */
    public Object[] getQueryObjArray(String sql) {
        Object[] dataObjs = null;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            if (rs.next()) {
                dataObjs = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    dataObjs[i] = rs.getObject(rsmd.getColumnName(i + 1));
                    if (log.isDebugEnabled()) {
                        log.debug("��ȡ�ֶ�:" + rsmd.getColumnName(i + 1) + "������:[" + dataObjs[i] + "] >>>��ע�뵽����������,��Ӧ�±�:"
                                + i);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("----->>�Ѷ�������Ž�һ��List������=" + Arrays.toString(dataObjs));
                    log.debug("----->>");
                }
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return dataObjs;
    }

    /**
     * ��ȡsql��ѯ�漰�����е�����
     * 
     * @param sql String
     * @return List ���������ֶε�����
     */
    public List getColumnNameInList(String sql) {
        List list = new ArrayList();
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String columnName = rsmd.getColumnName(i);
                list.add(columnName);
            }
            return list;
        } catch (Exception e) {
            throw new RecRunTimeException(e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
    }

    /**
     * ��SQL��ѯ���÷�װ��һ�����������У���������List����<br>
     * ���SQL��ѯ����û�м�¼���򷵻ص�List���϶���isEmpty()Ϊtrue
     * 
     * @param sql SQL�������
     * @return List
     */
    public List getQueryObjArrayInList(String sql) {
        List list = new ArrayList();
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("ִ��SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            while (rs.next()) {
                Object[] dataObjs = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    dataObjs[i] = rs.getObject(rsmd.getColumnName(i + 1));
                    if (log.isDebugEnabled()) {
                        log.debug("��ȡ�ֶ�[" + rsmd.getColumnName(i + 1) + "]������:" + dataObjs[i] + " >>>��ע�뵽����������,��Ӧ�±�:"
                                + i);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("----->>�Ѷ�������Ž�һ��List������=" + Arrays.toString(dataObjs));
                    log.debug("----->>>");
                }
                list.add(dataObjs);
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return list;
    }

    // ��ѯ�����cachekeyǰ׺�ַ���
    private static final String QUERY_CACHEKEY_LCL = "SESSION_QUERY:";

    private void cacheFiller(Class cla) {
        GlobalCache cache = CacheManager.getGlobalCache();
        String cacheKey = QUERY_CACHEKEY_LCL + cla.getName();
        Map beanCacheMap = new HashMap();
        Field[] fields = cla.getDeclaredFields();
        if (log.isDebugEnabled()) {
            log.debug("������" + cla.getName() + "���仺�������Ϣ:::");
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String setterMethodName = "set" + StringUtil.initCap(field.getName());
            Method setterMethod = null;
            try {
                setterMethod = cla.getMethod(setterMethodName, new Class[] { field.getType() });
            } catch (Exception e) {
                throw new RecRunTimeException(e);
            }
            CacheParam cp = new CacheParam();
            cp.setFieldName(field.getName());
            cp.setSetterMethodObj(setterMethod);
            cp.setParamClassName(field.getType().getName());
            beanCacheMap.put(field.getName(), cp);
            if (log.isDebugEnabled()) {
                log.debug("��" + cla.getName() + "���ֶ�" + field.getName() + "���л���...");
            }
        }
        cache.addProperty(cacheKey, beanCacheMap);
    }

    private Object getReturnObjByQueryObjList(ResultSet rs, String fieldName, String paramType) throws SQLException {
        Object retObj = null;
        if (paramType.equalsIgnoreCase("int")) {
            retObj = new Integer(rs.getInt(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Integer")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Integer(tempObj.toString());
            }
        } else if (paramType.equalsIgnoreCase("java.lang.String")) {
            retObj = rs.getString(fieldName) == null ? "" : rs.getString(fieldName);
        } else if (paramType.equalsIgnoreCase("double")) {
            retObj = new Double(rs.getDouble(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Double")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Double(tempObj.toString());
            }
        } else if (paramType.equalsIgnoreCase("com.recolar.service.RecDate")) {
            retObj = rs.getDate(fieldName) == null ? null : new RecDate(rs.getTimestamp(fieldName).getTime());
        } else if (paramType.equalsIgnoreCase("java.sql.Date")) {
            retObj = rs.getDate(fieldName);
        } else if (paramType.equalsIgnoreCase("java.sql.Timestamp")) {
            retObj = rs.getTimestamp(fieldName);
        } else if (paramType.equalsIgnoreCase("java.sql.Time")) {
            retObj = rs.getTime(fieldName);
        } else if (paramType.equalsIgnoreCase("java.util.Date")) {
            retObj = rs.getObject(fieldName);
        } else if (paramType.equalsIgnoreCase("float")) {
            retObj = new Float(rs.getFloat(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Float")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Float(tempObj.toString());
            }
        } else if (paramType.equalsIgnoreCase("byte")) {
            retObj = new Byte(rs.getByte(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Byte")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Byte(tempObj.toString());
            }
        } else if (paramType.equalsIgnoreCase("short")) {
            retObj = new Short(rs.getShort(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Short")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Short(tempObj.toString());
            }
        } else if (paramType.equalsIgnoreCase("long")) {
            retObj = new Long(rs.getLong(fieldName));
        } else if (paramType.equalsIgnoreCase("java.lang.Long")) {
            Object tempObj = rs.getObject(fieldName);
            if (tempObj != null) {
                retObj = new Long(tempObj.toString());
            }
        } else if ("java.math.BigDecimal".equals(paramType)) {
            retObj = rs.getBigDecimal(fieldName);
        } else if ("java.math.BigInteger".equals(paramType)) {
            retObj = rs.getBigDecimal(fieldName).toBigInteger();
        } else {
            retObj = rs.getObject(fieldName);
        }
        return retObj;
    }

    /**
     * �ڲ�����,������JavaBean����̬�������ݿ��¼�ķ����е���<br>
     * ����ResultSet�ж�̬update��
     * 
     * @param rs ResultSet����
     * @param fieldName�ֶ�����
     * @param cla JavaBean�����Class
     */
    private void setDataToResultSet(ResultSet rs, String fieldName, Object obj) {
        Method[] mets = obj.getClass().getDeclaredMethods();
        try {
            for (int i = 0; i < mets.length; i++) {
                Method met = mets[i];// ����Method����
                String metName = met.getName();
                if (("get" + fieldName).equalsIgnoreCase(metName)) {
                    String retTypeName = met.getReturnType().toString();// getter�����ķ�������
                    Object retValue = met.invoke(obj, new Object[] {});// ��ȡgetter�����ķ���ֵ
                    if (retTypeName.equalsIgnoreCase("int")) {
                        int value = ((Integer) retValue).intValue();
                        rs.updateInt(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("double")) {
                        double value = ((Double) retValue).doubleValue();
                        rs.updateDouble(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("class java.lang.String")) {
                        String value = (String) retValue;
                        rs.updateString(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("float")) {
                        float value = ((Float) retValue).floatValue();
                        rs.updateFloat(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("byte")) {
                        byte value = ((Byte) retValue).byteValue();
                        rs.updateByte(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("short")) {
                        short value = ((Short) retValue).shortValue();
                        rs.updateShort(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("long")) {
                        long value = ((Long) retValue).longValue();
                        rs.updateLong(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("class com.recolar.service.RecDate")) {
                        RecDate value = (RecDate) retValue;
                        rs.updateDate(fieldName, value.getSqlDateObject());
                    } else if (retTypeName.equalsIgnoreCase("class java.sql.Date")) {
                        java.sql.Date value = (java.sql.Date) retValue;
                        rs.updateDate(fieldName, value);
                    } else if (retTypeName.equalsIgnoreCase("class java.sql.Timestamp")) {
                        Timestamp value = (Timestamp) retValue;
                        rs.updateTimestamp(fieldName, value);
                    }
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * ����:executeBatchSql(String sql, Object[] objs, ParamCon pc)ר��<br>
     * �ڲ�����
     * 
     * @param ppst PreparedStatement����
     * @param object JavaBean����
     * @param pc ParamCon����
     */
    private boolean setPreparedStatementParam(PreparedStatement ppst, Object object, ParamCon pc, boolean isBatchExecute) {
        boolean isSucceed = true;
        try {
            for (int i = 0; i < pc.getParamLength(); i++) {
                int index = pc.getParamObject(i).getIndex();// �ֶ��±�
                String methodName = "get" + StringUtil.initCap(pc.getParamObject(i).getTypeName());// ��������
                int paramType = pc.getParamObject(i).getParamType();// ��������
                Method me = object.getClass().getMethod(methodName, new Class[] {});
                Object retData = me.invoke(object, new Object[] {});
                if (paramType == ParamTypes.PARAM_byte) {
                    ppst.setByte(index, ((Byte) retData).byteValue());
                } else if (paramType == ParamTypes.PARAM_BYTE) {
                    if (retData != null)
                        ppst.setByte(index, ((Byte) retData).byteValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_short) {
                    ppst.setShort(index, ((Short) (retData)).shortValue());
                } else if (paramType == ParamTypes.PARAM_SHORT) {
                    if (retData != null)
                        ppst.setShort(index, ((Byte) retData).byteValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_int) {
                    ppst.setInt(index, ((Integer) (retData)).intValue());
                } else if (paramType == ParamTypes.PARAM_INTEGER) {
                    if (retData != null)
                        ppst.setInt(index, ((Integer) (retData)).intValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_long) {
                    ppst.setLong(index, ((Long) (retData)).longValue());
                } else if (paramType == ParamTypes.PARAM_LONG) {
                    if (retData != null)
                        ppst.setLong(index, ((Long) (retData)).longValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_float) {
                    ppst.setFloat(index, ((Float) (retData)).floatValue());
                } else if (paramType == ParamTypes.PARAM_FLOAT) {
                    if (retData != null)
                        ppst.setFloat(index, ((Float) (retData)).floatValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_double) {
                    ppst.setDouble(index, ((Double) (retData)).doubleValue());
                } else if (paramType == ParamTypes.PARAM_DOUBLE) {
                    if (retData != null)
                        ppst.setDouble(index, ((Double) (retData)).doubleValue());
                    else
                        ppst.setBigDecimal(index, null);
                } else if (paramType == ParamTypes.PARAM_BIGDECIMAL || paramType == ParamTypes.PARAM_BIGINTEGER) {
                    ppst.setBigDecimal(index, retData == null ? null : (BigDecimal) retData);
                } else if (paramType == ParamTypes.PARAM_STRING) {
                    ppst.setString(index, (String) (retData));
                } else if (paramType == ParamTypes.PARAM_UTILDATE) {
                    ppst.setDate(index, new Date(((java.util.Date) (retData)).getTime()));
                } else if (paramType == ParamTypes.PARAM_SQLDATE) {
                    ppst.setDate(index, (java.sql.Date) retData);
                } else if (paramType == ParamTypes.PARAM_TIMESTAMP) {
                    ppst.setTimestamp(index, (java.sql.Timestamp) (retData));
                } else if (paramType == ParamTypes.PARAM_RECDATE) {
                    if (retData != null) {
                        RecDate rDate = (RecDate) retData;
                        if (rDate.isDateType()) {
                            ppst.setDate(index, rDate.getSqlDateObject());
                        } else {
                            ppst.setTimestamp(index, rDate.getTimestampObject());
                        }
                    } else {
                        ppst.setTimestamp(index, null);
                    }
                } else if (paramType == ParamTypes.PARAM_OBJECT) {
                    ppst.setObject(index, retData);
                }
            }// ��������
             // �����������
            if (isBatchExecute)
                ppst.addBatch();
        } catch (Exception e) {
            isSucceed = false;
            throw new RecRunTimeException(e);
        }
        return isSucceed;
    }

    /**
     * ��һ�����ݿ�����<br>
     * ���ṩ��Connection������������
     * 
     * @param conn Connection����
     */
    public DBSessionImpl open(Connection conn) {
        if (log.isDebugEnabled()) {
            log.debug("��һ��Connection���Ӷ��������DBSession��");
        }
        this.conn = conn;
        // try {
        // if (this.conn.getAutoCommit()) {
        // this.conn.setAutoCommit(false);
        // if (log.isDebugEnabled()) {
        // log.debug("��DBSession�ڲ������õ�Connection�����Զ������ύ����Ϊfalse");
        // }
        // }
        // } catch (Exception e) {
        // throw new RecRunTimeException(e);
        // }
        return this;
    }

    /**
     * �����Ƿ��Զ��ύ����
     * 
     * @param auto
     */
    public DBSessionImpl setAutoCommit(boolean auto) {
        try {
            this.conn.setAutoCommit(auto);
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        }
        return this;
    }

    /**
     * �ύ����<br>
     * �������ڲ����õ�Connection.commit();
     */
    public void commit() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("--->>>>�ύDBSession����...");
            }
            this.conn.commit();
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        }
    }

    /**
     * �ر����ݿ�������Դ
     */
    public void close() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("�ͷ�DBSession��Connection��Դ...");
            }
            this.conn = null;
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        }
    }

    /**
     * �ύ���񲢹ر�
     */
    public void commitAndClose() {
        commit();
        close();
    }

}
