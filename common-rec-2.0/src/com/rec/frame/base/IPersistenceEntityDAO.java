package com.rec.frame.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * ����DAO�ӿ�.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-19
 * @since JDK1.6.0
 * 
 * @param <T>
 */
public interface IPersistenceEntityDAO {

	/**
	 * �־û�ʵ�������ݿ�.
	 * 
	 * @param t ʵ�����
	 */
	public void save(final Object entity);

	/**
	 * ����ʵ��.
	 * 
	 * @param t ʵ�����
	 */
	public void update(final Object entity);

	/**
	 * ɾ��ʵ��.
	 * 
	 * @param t ʵ�����
	 */
	public void delete(final Object entity);

	/**
	 * ��������ID����ʵ��.
	 * 
	 * @param id ����
	 */
	public <T> T findByID(final Class<T> clazz, final Serializable id);

	/**
	 * ����ʵ������ֵ����ʵ�弯��.
	 * 
	 * @param propertyName ʵ����������
	 * @param value ʵ������ֵ
	 * @return ʵ�����List����
	 */
	public <T> List<T> findByProperty(final Class<T> clazz, final String propertyName, final Object value);

	/**
	 * ���ݶ��ʵ������ֵ����ʵ�弯��.
	 * 
	 * @param propMap ����ʵ�����Ժ�ֵ��Map����
	 * @return List<T>
	 */
	public <T> List<T> findByProperties(final Class<T> clazz, final Map<String, Object> propMap);

	/**
	 * ��������ʵ�弯��.
	 * 
	 * @param t ʵ�����
	 */
	public <T> List<T> findAll(final Class<T> clazz);

	/**
	 * �������ݱ�������������.
	 * 
	 * @param tableName ���ݱ�����
	 * @param tableAliasName ���ݱ����
	 * @param filterCondition ��ѯ��������
	 * @return ������
	 */
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition, final Object[] args);

	/**
	 * �������ݱ�������������.
	 * 
	 * @param tableName ���ݱ�����
	 * @param tableAliasName ���ݱ����
	 * @param filterCondition ��ѯ��������
	 * @return ������
	 */
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition);

	/**
	 * ��ѯ���������.
	 * 
	 * @param entityName PO����ʵ����
	 * @param entityAliasName ����
	 * @param filterCondition ��������
	 * @return ������
	 */
	Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition,
			Map<String, Object> argsMap);

	/**
	 * 
	 * @param entityName PO����ʵ����
	 * @param entityAliasName ����
	 * @param filterCondition ��������
	 * @param params ���������еı���
	 * @return ������
	 */
	Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition, List<Object> params);

	/**
	 * ������������.
	 * 
	 * @param entities List
	 */
	void batchSave(List<?> entities);
}
