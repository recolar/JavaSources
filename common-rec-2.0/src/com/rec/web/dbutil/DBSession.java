package com.rec.web.dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.rec.web.dbpl.ParamCon;

public interface DBSession {

	/**
	 * 结果集可滚动，但对底层数据表中所作的变化不敏感
	 */
	public static final int TYPE_CANMOVE_NOTSENCE = ResultSet.TYPE_SCROLL_INSENSITIVE;

	/**
	 * 结果集可滚动，对顶层数据表中所作的变化敏感
	 */
	public static final int TYPE_CANMOVE_SENCE = ResultSet.TYPE_SCROLL_SENSITIVE;

	/**
	 * 结果集不可更新，能够提供最大可能的并发级别
	 */
	public static final int CONCUR_CANUPDATE = ResultSet.CONCUR_READ_ONLY;

	/**
	 * 结果集可更新，只能够提供受限的并发级别
	 */
	public static final int CONCUR_CANNOTUPDATE = ResultSet.CONCUR_UPDATABLE;

	/**
	 * 求最大值
	 */
	public static final String GETMAX = "Max({0})";

	/**
	 * 求最小值
	 */
	public static final String GETMIN = "Min({0})";

	/**
	 * 求平均值
	 */
	public static final String GETAVG = "Avg({0})";

	/**
	 * 求总和值
	 */
	public static final String GETSUM = "Sum({0})";

	/**
	 * 求数量值
	 */
	public static final String GETCOUNT = "Count({0})";

	/**
	 * 执行SQL语句,返回所受影响的行数<br>
	 * 
	 * @param sql SQL增删改语句
	 * @return int 如果返回为-1则表示运行时出现异常
	 */
	public int executeSQL(String sql);

	/**
	 * 检测是否存在某一条记录
	 * 
	 * @param sql 查询SQL
	 * @return true:存在 false:不存在
	 */
	public boolean getExistRecord(String sql);

	/**
	 * 插入一行记录进数据库中去，其中obj参数是要插入记录的JavaBean对象
	 * 
	 * @param sql 插入数据库的SQL语句<br>
	 *            select语句，则支持字段别名,但别名名称必须和JavaBean的属性名称一致<br>
	 * 
	 * @param obj JavaBean对象,封装了对一张数据表的getter和setter方法
	 * @return 插入成功返回true否则返回false
	 */
	public boolean insertBeanDataIntoDB(String sql, Object obj);

	/**
	 * 将单据JavaBean对象插入表,支持insert和update语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param sql Sql语句
	 * @param obj JavaBean对象
	 * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
	 * @return true:执行成功
	 */
	public boolean insertOrUpdateBeanDataIntoDB(String sql, Object obj, ParamCon pc);

	/**
	 * 将单据JavaBean对象插入表,支持insert和update语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param pc ParamCon对象
	 * @return true:执行成功
	 */
	public boolean insertOrUpdateBeanDataIntoDB(ParamCon pc);

	/**
	 * 将数据插入到数据库中,支持批量插入<br>
	 * 在ParamCon中putFieldValue设置插入的字段名称以及插入值,设置完之后需要调用addFieldValueBatch()方法
	 * 
	 * @param pc ParamCon 数据库操纵封装对象
	 * @return boolean 插入成功返回true，反之返回false
	 */
	public boolean insertDataIntoDB(ParamCon pc);

	/**
	 * 获取sql查询涉及到的列的名称
	 * 
	 * @param sql String
	 * @return List 包含各个字段的名称
	 */
	public List getColumnNameInList(String sql);

	/**
	 * 获取分组信息,在Sql参数中已经写好了分组信息的取数逻辑<br>
	 * 比如可以获取sql查询语句中的结果行数<br>
	 * 注意的是sql参数的所得结果只能是一行一列
	 * 
	 * @param sql 查询语句
	 * @return String 分组结果.如果结果是数字的话要类型转换
	 */
	public String getGroupValueBySql(String sql);

	/**
	 * 获取分组信息.比如在某个table中要获取某列的max,min,avg,sum,count等等信息<br>
	 * 返回的是字符串变量,实际使用中要记得类型转换.<br>
	 * 注:只能获取一行一列的分组信息
	 * 
	 * @param tableName 表名称
	 * @param colName 需要获取分组信息的列
	 * @param groupFlag 分组动作标志
	 * @return 分组结果
	 */
	public String getGroupValue(String tableName, String colName, String groupFlag);

	/**
	 * 获取分组信息.比如在某个table中要获取某列的max,min,avg,sum,count等等信息<br>
	 * 返回的是字符串变量,实际使用中要记得类型转换.<br>
	 * 注:只能获取一行一列的分组信息
	 * 
	 * @param tableName 表名称
	 * @param filterSql 过滤SQL字符串(Where字句),如:Where Typeid=0 group by EmpNo
	 * @param colName 需要获取分组信息的列
	 * @param groupFlag 分组动作标志
	 * @return 分组结果
	 */
	public String getGroupValue(String tableName, String filterSql, String colName, String groupFlag);

	/**
	 * 根据表名获取该表的行数
	 * 
	 * @return int 行数
	 */
	public int getTableRows(String tableName);

	/**
	 * 执行批处理的方法，返回所收影响的行数
	 * 
	 * @param batchSql 用于执行批处理的sql语句字符串数组
	 * @return 返回所收影响的行数，如果异常则返回-1
	 * @throws Exception
	 */
	public int executeBatchSql(String[] batchSql);

	/**
	 * 执行批处理SQL的方法
	 * 
	 * @param sql 注意:必须为Select语句<br>
	 *            比如要插入表Users的列为ID,USERNAME,AGE,则必须写成select ID,USERNAME,AGE From users
	 * @param objs 对应要插入表的JavaBean对象(Vo对象)
	 * @return 插入数据库记录的行数,返回-1则表示失败
	 */
	public int executeBatchSql(String sql, Object[] objs);

	/**
	 * 执行批处理语句,只支持insert语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param sql Sql语句
	 * @param objs JavaBean对象数组
	 * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
	 * @return 返回成功插入数据库的条数
	 */
	public int executeBatchSql(String sql, Object[] objs, ParamCon pc);

	/**
	 * 执行批处理语句,只支持insert语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
	 * 
	 * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
	 * @return 返回成功插入数据库的条数
	 */
	public int executeBatchSql(ParamCon pc);

	/**
	 * 获取查询结果Map，Map对象的Key保存字段名称，Value保存字段值<br>
	 * 只对Select查询语句有效,注意Map的key保存字段名称时记得要和查询语句中的字段名称相一致<br>
	 * 主要是查询某一行的数据。例如用户登录验证。如果sql返回多条信息则只保存第一条信息<br>
	 * 如果返回的map.isEmpty()==true则查询不到数据
	 * 
	 * @param sql 查询SQL语句
	 * @return Map 对象,封装了查询结果
	 */
	public Map getQueryMap(String sql);

	/**
	 * 获取查询数据列表信息，List保存Map对象,Map对象的Key保存字段名称，Value保存字段值<br>
	 * 只对Select查询语句有效,注意Map的key保存字段名称时记得要和查询语句中的字段名称相一致
	 * 
	 * @param sql 查询SQL语句
	 * @return List 对象
	 */
	public List getQueryListMap(String sql);

	/**
	 * 根据查询SQL语句所得结果封装到一个JavaVo对象里面去并返回此对象<br>
	 * 只取一行数据。如果sql返回多行数据则取第一行
	 * 
	 * @param sql 查询语句
	 * @param cla Class对象
	 * @return Object 一个Vo对象,如果返回为null则该查询语句没有查询所得结果
	 */
	public Object getQueryObj(String sql, Class cla);

	/**
	 * 把SQL查询所得封装到一个对象数组中<br>
	 * 注意:此SQL查询如果返回条记录，则对象数组封装了此记录<br>
	 * 如果此SQL查询返回的是多行记录，则对象数组封装了第一行记录<br>
	 * 如果查询所得没有记录，则返回null
	 * 
	 * @param sql SQL查许语句
	 * @return Object[]
	 */
	public Object[] getQueryObjArray(String sql);

	/**
	 * 把SQL查询所得封装到一个对象数组中，此数组用List保存<br>
	 * 如果SQL查询所得没有记录，则返回的List集合对象isEmpty()为true
	 * 
	 * @param sql SQL查许语句
	 * @return List
	 */
	public List getQueryObjArrayInList(String sql);

	/**
	 * 根据SQL查询语句获取对象Vo的List
	 * 
	 * @param sql 查询语句
	 * @param cla Class对象
	 * @return List集合对象
	 */
	public List getQueryObjList(String sql, Class cla);

	/**
	 * 打开一个数据库连接
	 * 
	 * @param conn Connection对象
	 */
	public DBSessionImpl open(Connection conn);

	/**
	 * 关闭数据库连接资源
	 * 
	 */
	public void close();

	/**
	 * 提交事务
	 */
	public void commit();

}