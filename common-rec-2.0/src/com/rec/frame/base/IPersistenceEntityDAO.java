package com.rec.frame.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 抽象DAO接口.
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
	 * 持久化实体至数据库.
	 * 
	 * @param t 实体对象
	 */
	public void save(final Object entity);

	/**
	 * 更新实体.
	 * 
	 * @param t 实体对象
	 */
	public void update(final Object entity);

	/**
	 * 删除实体.
	 * 
	 * @param t 实体对象
	 */
	public void delete(final Object entity);

	/**
	 * 根据主键ID检索实体.
	 * 
	 * @param id 主键
	 */
	public <T> T findByID(final Class<T> clazz, final Serializable id);

	/**
	 * 根据实体属性值检索实体集合.
	 * 
	 * @param propertyName 实体属性名称
	 * @param value 实体属性值
	 * @return 实体对象List集合
	 */
	public <T> List<T> findByProperty(final Class<T> clazz, final String propertyName, final Object value);

	/**
	 * 根据多个实体属性值检索实体集合.
	 * 
	 * @param propMap 保存实体属性和值的Map对象
	 * @return List<T>
	 */
	public <T> List<T> findByProperties(final Class<T> clazz, final Map<String, Object> propMap);

	/**
	 * 返回所有实体集合.
	 * 
	 * @param t 实体对象
	 */
	public <T> List<T> findAll(final Class<T> clazz);

	/**
	 * 根据数据表名返回总行数.
	 * 
	 * @param tableName 数据表名称
	 * @param tableAliasName 数据表别名
	 * @param filterCondition 查询过滤条件
	 * @return 总行数
	 */
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition, final Object[] args);

	/**
	 * 根据数据表名返回总行数.
	 * 
	 * @param tableName 数据表名称
	 * @param tableAliasName 数据表别名
	 * @param filterCondition 查询过滤条件
	 * @return 总行数
	 */
	public Integer getTotalRowCountBySQL(final String tableName, final String tableAliasName,
			final String filterCondition);

	/**
	 * 查询结果总行数.
	 * 
	 * @param entityName PO对象实体名
	 * @param entityAliasName 别名
	 * @param filterCondition 过滤条件
	 * @return 总行数
	 */
	Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition,
			Map<String, Object> argsMap);

	/**
	 * 
	 * @param entityName PO对象实体名
	 * @param entityAliasName 别名
	 * @param filterCondition 过滤条件
	 * @param params 过滤条件中的变量
	 * @return 总行数
	 */
	Integer getTotalRowCountByHQL(String entityName, String entityAliasName, String filterCondition, List<Object> params);

	/**
	 * 批量插入数据.
	 * 
	 * @param entities List
	 */
	void batchSave(List<?> entities);
}
