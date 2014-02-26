package com.rec.web.dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.rec.web.dbpl.ParamCon;

public interface DBSession {

	/**
	 * ������ɹ��������Եײ����ݱ��������ı仯������
	 */
	public static final int TYPE_CANMOVE_NOTSENCE = ResultSet.TYPE_SCROLL_INSENSITIVE;

	/**
	 * ������ɹ������Զ������ݱ��������ı仯����
	 */
	public static final int TYPE_CANMOVE_SENCE = ResultSet.TYPE_SCROLL_SENSITIVE;

	/**
	 * ��������ɸ��£��ܹ��ṩ�����ܵĲ�������
	 */
	public static final int CONCUR_CANUPDATE = ResultSet.CONCUR_READ_ONLY;

	/**
	 * ������ɸ��£�ֻ�ܹ��ṩ���޵Ĳ�������
	 */
	public static final int CONCUR_CANNOTUPDATE = ResultSet.CONCUR_UPDATABLE;

	/**
	 * �����ֵ
	 */
	public static final String GETMAX = "Max({0})";

	/**
	 * ����Сֵ
	 */
	public static final String GETMIN = "Min({0})";

	/**
	 * ��ƽ��ֵ
	 */
	public static final String GETAVG = "Avg({0})";

	/**
	 * ���ܺ�ֵ
	 */
	public static final String GETSUM = "Sum({0})";

	/**
	 * ������ֵ
	 */
	public static final String GETCOUNT = "Count({0})";

	/**
	 * ִ��SQL���,��������Ӱ�������<br>
	 * 
	 * @param sql SQL��ɾ�����
	 * @return int �������Ϊ-1���ʾ����ʱ�����쳣
	 */
	public int executeSQL(String sql);

	/**
	 * ����Ƿ����ĳһ����¼
	 * 
	 * @param sql ��ѯSQL
	 * @return true:���� false:������
	 */
	public boolean getExistRecord(String sql);

	/**
	 * ����һ�м�¼�����ݿ���ȥ������obj������Ҫ�����¼��JavaBean����
	 * 
	 * @param sql �������ݿ��SQL���<br>
	 *            select��䣬��֧���ֶα���,���������Ʊ����JavaBean����������һ��<br>
	 * 
	 * @param obj JavaBean����,��װ�˶�һ�����ݱ��getter��setter����
	 * @return ����ɹ�����true���򷵻�false
	 */
	public boolean insertBeanDataIntoDB(String sql, Object obj);

	/**
	 * ������JavaBean��������,֧��insert��update���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param sql Sql���
	 * @param obj JavaBean����
	 * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
	 * @return true:ִ�гɹ�
	 */
	public boolean insertOrUpdateBeanDataIntoDB(String sql, Object obj, ParamCon pc);

	/**
	 * ������JavaBean��������,֧��insert��update���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param pc ParamCon����
	 * @return true:ִ�гɹ�
	 */
	public boolean insertOrUpdateBeanDataIntoDB(ParamCon pc);

	/**
	 * �����ݲ��뵽���ݿ���,֧����������<br>
	 * ��ParamCon��putFieldValue���ò�����ֶ������Լ�����ֵ,������֮����Ҫ����addFieldValueBatch()����
	 * 
	 * @param pc ParamCon ���ݿ���ݷ�װ����
	 * @return boolean ����ɹ�����true����֮����false
	 */
	public boolean insertDataIntoDB(ParamCon pc);

	/**
	 * ��ȡsql��ѯ�漰�����е�����
	 * 
	 * @param sql String
	 * @return List ���������ֶε�����
	 */
	public List getColumnNameInList(String sql);

	/**
	 * ��ȡ������Ϣ,��Sql�������Ѿ�д���˷�����Ϣ��ȡ���߼�<br>
	 * ������Ի�ȡsql��ѯ����еĽ������<br>
	 * ע�����sql���������ý��ֻ����һ��һ��
	 * 
	 * @param sql ��ѯ���
	 * @return String ������.�����������ֵĻ�Ҫ����ת��
	 */
	public String getGroupValueBySql(String sql);

	/**
	 * ��ȡ������Ϣ.������ĳ��table��Ҫ��ȡĳ�е�max,min,avg,sum,count�ȵ���Ϣ<br>
	 * ���ص����ַ�������,ʵ��ʹ����Ҫ�ǵ�����ת��.<br>
	 * ע:ֻ�ܻ�ȡһ��һ�еķ�����Ϣ
	 * 
	 * @param tableName ������
	 * @param colName ��Ҫ��ȡ������Ϣ����
	 * @param groupFlag ���鶯����־
	 * @return ������
	 */
	public String getGroupValue(String tableName, String colName, String groupFlag);

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
	public String getGroupValue(String tableName, String filterSql, String colName, String groupFlag);

	/**
	 * ���ݱ�����ȡ�ñ������
	 * 
	 * @return int ����
	 */
	public int getTableRows(String tableName);

	/**
	 * ִ��������ķ�������������Ӱ�������
	 * 
	 * @param batchSql ����ִ���������sql����ַ�������
	 * @return ��������Ӱ�������������쳣�򷵻�-1
	 * @throws Exception
	 */
	public int executeBatchSql(String[] batchSql);

	/**
	 * ִ��������SQL�ķ���
	 * 
	 * @param sql ע��:����ΪSelect���<br>
	 *            ����Ҫ�����Users����ΪID,USERNAME,AGE,�����д��select ID,USERNAME,AGE From users
	 * @param objs ��ӦҪ������JavaBean����(Vo����)
	 * @return �������ݿ��¼������,����-1���ʾʧ��
	 */
	public int executeBatchSql(String sql, Object[] objs);

	/**
	 * ִ�����������,ֻ֧��insert���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param sql Sql���
	 * @param objs JavaBean��������
	 * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
	 * @return ���سɹ��������ݿ������
	 */
	public int executeBatchSql(String sql, Object[] objs, ParamCon pc);

	/**
	 * ִ�����������,ֻ֧��insert���,sql�﷨��ʽΪinsert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param pc ParamCon����,���ڵ���insert���Ĳ�������Ϣ�����
	 * @return ���سɹ��������ݿ������
	 */
	public int executeBatchSql(ParamCon pc);

	/**
	 * ��ȡ��ѯ���Map��Map�����Key�����ֶ����ƣ�Value�����ֶ�ֵ<br>
	 * ֻ��Select��ѯ�����Ч,ע��Map��key�����ֶ�����ʱ�ǵ�Ҫ�Ͳ�ѯ����е��ֶ�������һ��<br>
	 * ��Ҫ�ǲ�ѯĳһ�е����ݡ������û���¼��֤�����sql���ض�����Ϣ��ֻ�����һ����Ϣ<br>
	 * ������ص�map.isEmpty()==true���ѯ��������
	 * 
	 * @param sql ��ѯSQL���
	 * @return Map ����,��װ�˲�ѯ���
	 */
	public Map getQueryMap(String sql);

	/**
	 * ��ȡ��ѯ�����б���Ϣ��List����Map����,Map�����Key�����ֶ����ƣ�Value�����ֶ�ֵ<br>
	 * ֻ��Select��ѯ�����Ч,ע��Map��key�����ֶ�����ʱ�ǵ�Ҫ�Ͳ�ѯ����е��ֶ�������һ��
	 * 
	 * @param sql ��ѯSQL���
	 * @return List ����
	 */
	public List getQueryListMap(String sql);

	/**
	 * ���ݲ�ѯSQL������ý����װ��һ��JavaVo��������ȥ�����ش˶���<br>
	 * ֻȡһ�����ݡ����sql���ض���������ȡ��һ��
	 * 
	 * @param sql ��ѯ���
	 * @param cla Class����
	 * @return Object һ��Vo����,�������Ϊnull��ò�ѯ���û�в�ѯ���ý��
	 */
	public Object getQueryObj(String sql, Class cla);

	/**
	 * ��SQL��ѯ���÷�װ��һ������������<br>
	 * ע��:��SQL��ѯ�����������¼������������װ�˴˼�¼<br>
	 * �����SQL��ѯ���ص��Ƕ��м�¼������������װ�˵�һ�м�¼<br>
	 * �����ѯ����û�м�¼���򷵻�null
	 * 
	 * @param sql SQL�������
	 * @return Object[]
	 */
	public Object[] getQueryObjArray(String sql);

	/**
	 * ��SQL��ѯ���÷�װ��һ�����������У���������List����<br>
	 * ���SQL��ѯ����û�м�¼���򷵻ص�List���϶���isEmpty()Ϊtrue
	 * 
	 * @param sql SQL�������
	 * @return List
	 */
	public List getQueryObjArrayInList(String sql);

	/**
	 * ����SQL��ѯ����ȡ����Vo��List
	 * 
	 * @param sql ��ѯ���
	 * @param cla Class����
	 * @return List���϶���
	 */
	public List getQueryObjList(String sql, Class cla);

	/**
	 * ��һ�����ݿ�����
	 * 
	 * @param conn Connection����
	 */
	public DBSessionImpl open(Connection conn);

	/**
	 * �ر����ݿ�������Դ
	 * 
	 */
	public void close();

	/**
	 * �ύ����
	 */
	public void commit();

}