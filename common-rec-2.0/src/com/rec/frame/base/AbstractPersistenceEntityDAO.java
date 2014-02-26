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
 * DAO抽象父类.位于顶层的数据访问层实现类，提供了常用的数据库持久化方法.
 * <p>
 * 只能用于继承，其中<code>hibernateTemplate</code>和<code>jdbcTemplate</code>两个属性需要派生类在构造函数中赋值.<br>
 * 一般可以在业务系统中再派生多一层，对以上两个属性赋值，然后所有的DAO都继承自该派生出来的中间DAO层.
 * 
 * @author Administrator
 * @version 1.0.0
 * @since JDK1.6.0
 * 
 */
public abstract class AbstractPersistenceEntityDAO implements IPersistenceEntityDAO {

	/** LOG. */
	protected static final Log LOG = LogFactory.getLog(AbstractPersistenceEntityDAO.class);
	/** 是否为Debug控制级别. */
	protected static final boolean ISDEBUG = LOG.isDebugEnabled();

	/** 是否为Info控制级别. */
	protected static final boolean ISINFO = LOG.isInfoEnabled();

	/** 是否为Warn控制级别. */
	protected static final boolean ISWARN = LOG.isWarnEnabled();

	/** 是否为ISERROR控制级别. */
	protected static final boolean ISERROR = LOG.isErrorEnabled();
	/**
	 * 定义SessionFactory.
	 */
	private SessionFactory sessionFactory;

	/**
	 * 定义JdbcTemplate.
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
	 * 获取Hibernate的Session对象.
	 * 
	 * @return Session
	 */
	public Session getSession() {
		return getSessionFactory().getCurrentSession();
	}

	/**
	 * 获取Connection连接对象.
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
	 * 持久化实体至数据库.
	 * 
	 * @param t 实体对象
	 */
	public void save(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.save(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("保存实体>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("保存实体失败>>>" + entity, e);
			}
			throw e;
		}
	}

	private static final int BATCH_SAVE_SIZE = 200;

	/**
	 * 批量插入数据.
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
	 * 更新实体.
	 * 
	 * @param t 实体对象
	 */
	public void update(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.update(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("更新实体>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("更新实体失败>>>" + entity, e);
			}
			throw e;
		}
	}

	/**
	 * 删除实体.
	 * 
	 * @param t 实体对象
	 */
	public void delete(final Object entity) {
		try {
			final Session session = getSessionFactory().getCurrentSession();
			session.delete(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("删除实体>>>>>" + entity);
			}
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("删除实体失败>>>" + entity, e);
			}
			throw e;
		}
	}

	/**
	 * 根据主键ID检索实体.
	 * 
	 * @param id 主键
	 */
	@SuppressWarnings("unchecked")
	public <T> T findByID(final Class<T> clazz, final Serializable id) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("根据ID检索实体，ID=" + id);
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			Object obj = session.get(clazz, id);
			return (T) obj;
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("根据ID检索实体失败，ID=" + id, e);
			}
			throw e;
		}
	}

	/**
	 * 根据实体属性值检索实体集合.
	 * 
	 * @param propertyName 实体属性名称
	 * @param value 实体属性值
	 * @return 实体对象List集合
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperty(final Class<T> clazz, final String propertyName, final Object value) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("根据实体属性检索实体集合，属性名称=" + propertyName + "，属性值=" + value);
		}
		try {
			final Session session = getSessionFactory().getCurrentSession();
			final String entityName = clazz.getSimpleName();
			final String hql = "from " + entityName + " m where m." + propertyName + "=?";
			Query query = session.createQuery(hql).setEntity(0, value);
			return query.list();
		} catch (RuntimeException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("根据实体属性检索实体集合失败...", e);
			}
			throw e;
		}
	}

	/**
	 * 根据多个实体属性值检索实体集合.
	 * 
	 * @param propMap 保存实体属性和值的Map对象
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByProperties(final Class<T> clazz, final Map<String, Object> propMap) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("根据实体属性检索实体集合");
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
				LOG.error("根据实体属性检索实体集合失败...", e);
			}
			throw e;
		}
	}

	/**
	 * 返回所有实体集合.
	 * 
	 * @param t 实体对象
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(final Class<T> clazz) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("检索所有实体.EntityName=" + clazz.getSimpleName());
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
	 * 根据HQL检索对象列表.
	 * 
	 * @param clazz Class
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> findByHQL(final Class<T> clazz, final String hql) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("根据HQL检索实体列表.EntityName=" + clazz.getSimpleName() + "，HQL=" + hql);
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
	 * 根据数据表名以及过滤条件返回总行数.
	 * 
	 * @param tableName 数据表名称
	 * @param filterCondition 查询过滤条件
	 * @return 总行数
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
	 * 根据数据表名返回总行数.
	 * 
	 * @param tableName 数据表名称
	 * @param tableAliasName 数据表别名
	 * @param filterCondition 查询过滤条件
	 * @return 总行数
	 */
	@Override
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition) {
		return getTotalRowCountBySQL(tableName, tableAliasName, filterCondition, new Object[] {});
	}

	/**
	 * 查询结果总行数.
	 * 
	 * @param entityName PO对象实体名
	 * @param entityAliasName 别名
	 * @param filterCondition 过滤条件
	 * @return 总行数
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
	 * @param entityName PO对象实体名
	 * @param entityAliasName 别名
	 * @param filterCondition 过滤条件
	 * @param params 过滤条件中的变量
	 * @return 总行数
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
	 * 根据提供的目标数据表名称，返回该表的所有字段名称.
	 * 
	 * @param tableName 目标数据表名.
	 * @return List 数据表所有字段集合对象.
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
