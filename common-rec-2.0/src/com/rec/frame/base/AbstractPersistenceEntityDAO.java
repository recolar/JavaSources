package com.rec.frame.base;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * DAO������.λ�ڶ�������ݷ��ʲ�ʵ���࣬�ṩ�˳��õ����ݿ�־û�����.
 * <p>
 * ֻ�����ڼ̳У�����<code>hibernateTemplate</code>��<code>jdbcTemplate</code>����������Ҫ�������ڹ��캯���и�ֵ.<br>
 * һ�������ҵ��ϵͳ����������һ�㣬�������������Ը�ֵ��Ȼ�����е�DAO���̳��Ը������������м�DAO��.
 * 
 * @author Administrator
 * @version 1.0.0
 * @since JDK1.6.0
 * 
 */
public abstract class AbstractPersistenceEntityDAO implements IPersistenceEntityDAO {

	/** LOG. */
	protected static final Log LOG = LogFactory.getLog(AbstractPersistenceEntityDAO.class);
	/** �Ƿ�ΪDebug���Ƽ���. */
	protected static final boolean ISDEBUG = LOG.isDebugEnabled();

	/** �Ƿ�ΪInfo���Ƽ���. */
	protected static final boolean ISINFO = LOG.isInfoEnabled();

	/** �Ƿ�ΪWarn���Ƽ���. */
	protected static final boolean ISWARN = LOG.isWarnEnabled();

	/** �Ƿ�ΪISERROR���Ƽ���. */
	protected static final boolean ISERROR = LOG.isErrorEnabled();
	/**
	 * ����SessionFactory.
	 */
	private SessionFactory sessionFactory;

	/**
	 * ����JdbcTemplate.
	 */
	private JdbcTemplate jdbcTemplate;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public AbstractPersistenceEntityDAO() {
	}

	public AbstractPersistenceEntityDAO(SessionFactory sessionFactory, JdbcTemplate jdbcTemplate) {
		this.sessionFactory = sessionFactory;
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * ��ȡHibernate��Session����.
	 * 
	 * @return Session
	 */
	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * ��ȡConnection���Ӷ���.
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		try {
			return this.jdbcTemplate.getDataSource().getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * �־û�ʵ�������ݿ�.
	 * 
	 * @param t ʵ�����
	 */
	public void save(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.save(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("����ʵ��>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("����ʵ��ʧ��>>>" + entity, e);
			}
			throw e;
		}
	}

	private static final int BATCH_SAVE_SIZE = 200;

	/**
	 * ������������.
	 * 
	 * @param entities List
	 */
	@Override
	public void batchSave(final List<?> entities) {
		final Session session = getSession();
		final int batchSize = BATCH_SAVE_SIZE;
		int i = 0;
		for (Object entity : entities) {
			session.save(entity);
			i++;
			if (i == batchSize) {
				session.flush();
				session.clear();
				i = 0;
			}
		}
		if (i != 0) {
			session.flush();
			session.clear();
		}
	}

	/**
	 * ����ʵ��.
	 * 
	 * @param t ʵ�����
	 */
	public void update(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.update(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("����ʵ��>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("����ʵ��ʧ��>>>" + entity, e);
			}
			throw e;
		}
	}

	/**
	 * ɾ��ʵ��.
	 * 
	 * @param t ʵ�����
	 */
	public void delete(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.delete(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("ɾ��ʵ��>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("ɾ��ʵ��ʧ��>>>" + entity, e);
			}
			throw e;
		}
	}

	/**
	 * ��������ID����ʵ��.
	 * 
	 * @param id ����
	 */
	@SuppressWarnings("unchecked")
	public <T> T findByID(final Class<T> clazz, final Serializable id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("����ID����ʵ�壬ID=" + id);
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			Object obj = session.get(clazz, id);
			return (T) obj;
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("����ID����ʵ��ʧ�ܣ�ID=" + id, e);
			}
			throw e;
		}
	}

	/**
	 * ����ʵ������ֵ����ʵ�弯��.
	 * 
	 * @param propertyName ʵ����������
	 * @param value ʵ������ֵ
	 * @return ʵ�����List����
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperty(final Class<T> clazz, final String propertyName, final Object value) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("����ʵ�����Լ���ʵ�弯�ϣ���������=" + propertyName + "������ֵ=" + value);
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			final String entityName = clazz.getSimpleName();
			final String hql = "from " + entityName + " m where m." + propertyName + "=?";
			Query query = session.createQuery(hql).setEntity(0, value);
			return query.list();
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("����ʵ�����Լ���ʵ�弯��ʧ��...", e);
			}
			throw e;
		}
	}

	/**
	 * ���ݶ��ʵ������ֵ����ʵ�弯��.
	 * 
	 * @param propMap ����ʵ�����Ժ�ֵ��Map����
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperties(final Class<T> clazz, final Map<String, Object> propMap) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("����ʵ�����Լ���ʵ�弯��");
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			final String entityName = clazz.getSimpleName();
			final StringBuffer hql = new StringBuffer("from " + entityName + " m where 1=1 ");
			for (String propertyName : propMap.keySet()) {
				hql.append(" and m." + propertyName + "=:" + propertyName);
			}
			Query query = session.createQuery(hql.toString());
			query.setProperties(propMap);
			return query.list();
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("����ʵ�����Լ���ʵ�弯��ʧ��...", e);
			}
			throw e;
		}
	}

	/**
	 * ��������ʵ�弯��.
	 * 
	 * @param t ʵ�����
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final Class<T> clazz) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("��������ʵ��.EntityName=" + clazz.getSimpleName());
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			final String propertyName = clazz.getSimpleName();
			final String hql = "from " + propertyName;
			return session.createQuery(hql).list();
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * ����HQL���������б�.
	 * 
	 * @param clazz Class
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHQL(final Class<T> clazz, final String hql) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("����HQL����ʵ���б�.EntityName=" + clazz.getSimpleName() + "��HQL=" + hql);
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			return session.createQuery(hql).list();
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}
			throw new RuntimeException(e);
		}
	}

	/**
	 * �������ݱ����Լ�������������������.
	 * 
	 * @param tableName ���ݱ�����
	 * @param filterCondition ��ѯ��������
	 * @return ������
	 */
	@Override
	public Integer getTotalRowCountBySQL(String tableName, String tableAliasName, String filterCondition, Object[] args) {
		String sql = "";
		if (filterCondition.toUpperCase().trim().startsWith("WHERE")) {
			sql = "SELECT COUNT(*) CC FROM " + tableName + " " + tableAliasName + " " + filterCondition;
		} else if (filterCondition.toUpperCase().trim().startsWith("AND")) {
			sql = "SELECT COUNT(*) CC FROM " + tableName + " " + tableAliasName + " WHERE 1=1 " + filterCondition;
		} else {
			sql = "SELECT COUNT(*) CC FROM " + tableName + " " + tableAliasName + " WHERE " + filterCondition;
		}
		if (args.length == 0) {
			return getJdbcTemplate().queryForObject(sql, Integer.class);
		} else {
			return getJdbcTemplate().queryForObject(sql, args, Integer.class);
		}
	}

	/**
	 * �������ݱ�������������.
	 * 
	 * @param tableName ���ݱ�����
	 * @param tableAliasName ���ݱ����
	 * @param filterCondition ��ѯ��������
	 * @return ������
	 */
	@Override
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition) {
		return getTotalRowCountBySQL(tableName, tableAliasName, filterCondition, new Object[] {});
	}

	/**
	 * ��ѯ���������.
	 * 
	 * @param entityName PO����ʵ����
	 * @param entityAliasName ����
	 * @param filterCondition ��������
	 * @return ������
	 */
	@Override
	public Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition,
			Map<String, Object> argsMap) {
		final String hql = innerGetTotalRowCountHQL(entityName, entityAliasName, filterCondition);
		final Session session = getSession();
		@SuppressWarnings("unchecked")
		final List<Object> list = session.createQuery(hql).setProperties(argsMap).list();
		final Integer count = new Integer(String.valueOf(list.get(0)));
		return count;
	}

	/**
	 * 
	 * @param entityName PO����ʵ����
	 * @param entityAliasName ����
	 * @param filterCondition ��������
	 * @param params ���������еı���
	 * @return ������
	 */
	@Override
	public Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition,
			List<Object> params) {
		final String hql = innerGetTotalRowCountHQL(entityName, entityAliasName, filterCondition);
		final Session session = getSession();
		final Query query = session.createQuery(hql);
		for (int i = 0; i < params.size(); i++) {
			Object param = params.get(i);
			query.setParameter(i + 1, param);
		}
		@SuppressWarnings("unchecked")
		final List<Object[]> list = query.list();
		final Integer count = (Integer) (list.get(0)[0]);
		return count;
	}

	private String innerGetTotalRowCountHQL(String entityName, String entityAliasName, String filterCondition) {
		String hql;
		if (filterCondition.toUpperCase().trim().startsWith("WHERE")) {
			hql = "select count(*) From " + entityName + " " + entityAliasName + " " + filterCondition;
		} else if (filterCondition.toUpperCase().trim().startsWith("AND")) {
			hql = "select count(*) From " + entityName + " " + entityAliasName + " WHERE 1=1 " + filterCondition;
		} else {
			hql = "select count(*) From " + entityName + " " + entityAliasName + " WHERE " + filterCondition;
		}
		return hql;
	}

	/**
	 * �����ṩ��Ŀ�����ݱ����ƣ����ظñ�������ֶ�����.
	 * 
	 * @param tableName Ŀ�����ݱ���.
	 * @return List ���ݱ������ֶμ��϶���.
	 */
	public List<String> getColumnNamesListByTableName(final String tableName) {
		final String queryNullSQL = "SELECT * FROM " + tableName + " WHERE 1=2 ";
		Connection conn = null;
		PreparedStatement ppst = null;
		ResultSet rs = null;
		List<String> columnNamesList = new ArrayList<String>();
		try {
			conn = getConnection();
			ppst = conn.prepareStatement(queryNullSQL);
			rs = ppst.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				columnNamesList.add(rsmd.getColumnName(i));
			}
			return columnNamesList;
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}
			throw new RuntimeException(e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ppst);
			JdbcUtils.closeConnection(conn);
		}
	}
}
