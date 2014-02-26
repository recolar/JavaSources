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
     * 提供一个Connection对象构造DBSession实例<br>
     * Connection对象必须打开自动提交
     * 
     * @param conn Connection
     */
    DBSessionImpl(Connection conn) {
        this.conn = conn;
        // try {
        // if (this.conn.getAutoCommit()) {
        // this.conn.setAutoCommit(false);
        // if (log.isDebugEnabled()) {
        // log.debug("将DBSession内部所关联之Connection对象自动提交事务设置为false");
        // }
        // }
        // } catch (SQLException e) {
        // throw new RecRunTimeException(e);
        // }
    }

    /**
     * 执行SQL语句,返回所受影响的行数<br>
     * 
     * @param sql SQL增删改语句
     * @return int 如果返回为-1则表示运行时出现异常
     */
    public int executeSQL(String sql) {
        PreparedStatement ppst = null;
        int effectRow = 0;
        try {
            ppst = conn.prepareStatement(sql);
            effectRow = ppst.executeUpdate();
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
                log.info("所受影响行数:" + effectRow);
            }
        } catch (Exception e) {
            rollBack();
            throw new RecRunTimeException("执行SQL失败...", e);
        } finally {// 子句中作关闭数据库连接资源的操作
            ConnUtil.releaseConnResource(ppst);
        }
        return effectRow;// 返回受影响行数
    }

    /**
     * 内部发生异常时，事务回滚..
     */
    private void rollBack() {
        try {
            conn.rollback();
            if (log.isErrorEnabled()) {
                log.error("发生异常,事务将回滚!!!");
            }
        } catch (Exception e2) {
        }
    }

    /**
     * 检测是否存在某一条记录
     * 
     * @param sql 查询SQL
     * @return true:存在 false:不存在
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
                log.info("执行SQL:" + sql);
                log.info("是否存在某行记录:" + isExist);
            }
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return isExist;
    }

    /**
     * 插入一行记录进数据库中去，其中obj参数是要插入记录的JavaBean对象
     * 
     * @param sql 插入数据库的SQL语句<br>
     *            select语句，则支持字段别名,但别名名称必须和JavaBean的属性名称一致<br>
     * 
     * @param obj JavaBean对象,封装了对一张数据表的getter和setter方法
     * @return 插入成功返回true否则返回false
     */
    public boolean insertBeanDataIntoDB(String sql, Object obj) {
        boolean isOk = false;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("解析对象:" + obj.getClass().getName());
            }
            ppst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            if (log.isInfoEnabled())
                log.info("执行SQL:" + sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            if (log.isDebugEnabled())
                log.debug("获取数据库元数据信息(ResultSetMetaData),用于解析各列的数据类型并利用反射插入数据");
            rs.moveToInsertRow();
            for (int i = 1; i < rsmd.getColumnCount() + 1; i++) {
                String fieldName = rsmd.getColumnName(i);
                setDataToResultSet(rs, fieldName, obj);
            }
            rs.insertRow();// 执行插入记录的操作
            if (log.isDebugEnabled())
                log.debug("插入对象至表成功...(需要提交事务)");
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
     * 将单据JavaBean对象插入表,支持insert和update语句,<br>
     * sql语法格式为insert into table(aa,nn) values(?,?),或者<br>
     * update table set aa=?,bb=? where id=?
     * 
     * @param sql Sql语句
     * @param obj JavaBean对象
     * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
     * @return true:执行成功
     */
    public boolean insertOrUpdateBeanDataIntoDB(String sql, Object obj, ParamCon pc) {
        boolean isOk = false;
        PreparedStatement ppst = null;
        try {
            ppst = conn.prepareStatement(sql);
            if (log.isDebugEnabled()) {
                log.debug("解析ParamCon对象...");
                log.debug("所提供的Pojo实例:" + obj.getClass().getName());
                for (Iterator i = pc.getParamList().iterator(); i.hasNext();) {
                    ParamObject paramObj = (ParamObject) i.next();
                    log.debug("解析参数:" + paramObj.getIndex() + "-->" + paramObj.getTypeName().toUpperCase());
                }
            }
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
            }
            boolean isProOk = setPreparedStatementParam(ppst, obj, pc, false);
            if (!isProOk) {
                throw new RecRunTimeException("请检查各个传入参数是否正确或者ParamCon参数是否配置正确");
            }
            ppst.executeUpdate();
            if (log.isDebugEnabled()) {
                log.debug("插入数据成功(需要提交DBSession事务)!" + SysUtil.objectToString(obj, true, "\t"));
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
     * 将单据JavaBean对象插入表,支持insert和update语句,<br>
     * sql语法格式为insert into table(aa,nn) values(?,?),或者<br>
     * update table set aa=?,bb=? where id=?
     * 
     * @param pc ParamCon对象
     * @return true:执行成功
     */
    public boolean insertOrUpdateBeanDataIntoDB(ParamCon pc) {
        if (log.isDebugEnabled()) {
            log.debug("从ParamCon实例中获取所设置的sql以及JavaBean对象参数");
        }
        String sql = pc.getSql();
        Object classObj = pc.getClassObj();
        return insertOrUpdateBeanDataIntoDB(sql, classObj, pc);
    }

    /**
     * 将数据插入到数据库中,支持批量插入<br>
     * 在ParamCon中putFieldValue设置插入的字段名称以及插入值,设置完之后需要调用addFieldValueBatch()方法
     * 
     * @param pc ParamCon 数据库操纵封装对象
     * @return boolean 插入成功返回true，反之返回false
     */
    public boolean insertDataIntoDB(ParamCon pc) {
        PreparedStatement ppst = null;
        boolean succeed = false;
        try {
            String sql = buildInsertSql(pc);
            ppst = conn.prepareStatement(sql);
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
                log.info("使用了参数绑定对象传递参数插入到数据库中");
            }
            for (Iterator i = pc.getBatchFieldObjList().iterator(); i.hasNext();) {
                Map map = (Map) i.next();
                int index = 1;
                for (Iterator i2 = map.entrySet().iterator(); i2.hasNext();) {
                    Entry entry = (Entry) i2.next();
                    Object fieldValue = entry.getValue();
                    ppst.setObject(index++, fieldValue);
                    if (log.isDebugEnabled()) {
                        log.debug("字段名称:" + entry.getKey() + ",字段值:" + fieldValue);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("::::::::::::::插入批量标记...");
                }
                ppst.addBatch();
            }
            ppst.executeBatch();
            if (log.isDebugEnabled()) {
                log.debug("插入数据成功(需要提交DBSession事务)!");
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
     * 获取分组信息,在Sql参数中已经写好了分组信息的取数逻辑<br>
     * 比如可以获取sql查询语句中的结果行数<br>
     * 注意的是sql参数的所得结果只能是一行一列
     * 
     * @param sql 查询语句
     * @return String 分组结果.如果结果是数字的话要类型转换
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
     * 获取分组信息.比如在某个table中要获取某列的max,min,avg,sum,count等等信息<br>
     * 返回的是字符串变量,实际使用中要记得类型转换.<br>
     * 注:只能获取一行一列的分组信息
     * 
     * @param tableName 表名称
     * @param colName 需要获取分组信息的列
     * @param groupFlag 分组动作标志
     * @return 分组结果 如有异常返回""
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
                log.info("执行SQL:" + sql);
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
                log.info("执行SQL:" + sql);
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
     * 根据表名获取该表的行数
     * 
     * @return int 行数
     */
    public int getTableRows(String tableName) {
        String sql = "Select Count(*) Num From " + tableName;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
            }
            if (rs.next()) {
                return rs.getInt("Num");
            }
        } catch (SQLException e) {
            throw new RecRunTimeException("获取该表的行数失败......", e);
        } finally {
            ConnUtil.releaseConnResource(rs, ppst);
        }
        return 0;

    }

    /**
     * 执行批处理的方法，返回所收影响的行数
     * 
     * @param batchSql 用于执行批处理的sql语句字符串数组
     * @return 返回所收影响的行数，如果异常则返回-1
     * @throws Exception
     */
    public int executeBatchSql(String[] batchSql) {
        Statement ste = null;
        int effectRow = 0;
        try {
            ste = conn.createStatement();
            for (int i = 0; i < batchSql.length; i++) {
                ste.addBatch(batchSql[i]);// 添加到St中
            }
            if (log.isDebugEnabled()) {
                log.debug("执行批处理...");
            }
            for (int i = 0; i < batchSql.length; i++) {
                if (log.isInfoEnabled()) {
                    log.info("执行SQL(批处理):" + batchSql[i]);
                }
            }
            int[] nums = ste.executeBatch();// 执行批处理

            // 统计所有受影响的行数
            for (int i = 0; i < nums.length; i++) {
                effectRow += nums[i];
            }
            if (log.isDebugEnabled()) {
                log.debug("提交批处理...");
                log.debug("批处理中所受影响行数:" + effectRow);
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
     * 执行批处理SQL的方法
     * 
     * @param sql 注意:必须为Select语句<br>
     *            比如要插入表Users的列为ID,USERNAME,AGE,则必须写成select ID,USERNAME,AGE From
     *            users
     * @param objs 对应要插入表的JavaBean对象(Vo对象)
     * @return 插入数据库记录的行数,返回-1则表示失败
     */
    public int executeBatchSql(String sql, Object[] objs) {
        int effectRow = -1;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);// 可更新结果集
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
                log.info("将数据进行批处理插入数据库中...");
            }
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// 获取查询SQL的各个列名字
            for (int i = 0; i < objs.length; i++) {
                rs.moveToInsertRow();
                for (int j = 1; j < rsmd.getColumnCount() + 1; j++) {
                    setDataToResultSet(rs, rsmd.getColumnName(j), objs[i]);
                }
                if (log.isDebugEnabled()) {
                    log.debug("解析对象[" + (i + 1) + "]:" + objs[i].getClass().getName());
                }
                rs.insertRow();
            }
            effectRow = objs.length;
            if (log.isDebugEnabled()) {
                log.debug("批处理执行成功(需要提交DBSession事务)!共插入数据表中记录数:" + objs.length);
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
     * 执行批处理语句,只支持insert语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
     * 
     * @param sql Sql语句
     * @param objs JavaBean对象数组
     * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
     * @return 返回成功插入数据库的条数
     */
    public int executeBatchSql(String sql, Object[] objs, ParamCon pc) {
        int effectRow = -1;
        PreparedStatement ppst = null;
        try {
            ppst = conn.prepareStatement(sql);
            if (log.isDebugEnabled()) {
                log.debug("解析ParamCon对象...");
                log.debug("所提供的Pojo实例:" + objs[0].getClass().getName());
                for (Iterator i = pc.getParamList().iterator(); i.hasNext();) {
                    ParamObject paramObj = (ParamObject) i.next();
                    log.debug("解析参数:" + paramObj.getIndex() + "-->" + paramObj.getTypeName().toUpperCase());
                }
            }
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
            }
            for (int i = 0; i < objs.length; i++) {
                boolean isProOk = setPreparedStatementParam(ppst, objs[i], pc, true);
                if (!isProOk) {
                    throw new RecRunTimeException("添加到批处理错误，请检查各个传入参数是否正确或者ParamCon参数是否配置正确");
                }
            }
            ppst.executeBatch();
            if (log.isDebugEnabled()) {
                log.debug("批处理执行成功(需要提交DBSession事务)...");
                log.debug("批处理插入数据行数:" + objs.length + "行");
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
     * 执行批处理语句,只支持insert语句,sql语法格式为insert into table(aa,nn) values(?,?)<br>
     * 
     * @param pc ParamCon对象,用于调配insert语句的插入列信息与次序
     * @return 返回成功插入数据库的条数
     */
    public int executeBatchSql(ParamCon pc) {
        String sql = pc.getSql();
        Object[] objs = pc.getObjs();
        return executeBatchSql(sql, objs, pc);
    }

    /**
     * 获取查询结果Map，Map对象的Key保存字段名称，Value保存字段值<br>
     * 只对Select查询语句有效,注意Map的key保存字段名称时记得要和查询语句中的字段名称相一致<br>
     * 主要是查询某一行的数据。例如用户登录验证。如果sql返回多条信息则只保存第一条信息<br>
     * 如果返回的map.isEmpty()==true则查询不到数据
     * 
     * @param sql 查询SQL语句
     * @return Map 对象,封装了查询结果
     */
    public Map getQueryMap(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        Map map = new HashMap();
        try {
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// 获取数据集元数据信息
            int count = rsmd.getColumnCount();// 字段列数
            String[] fieldList = new String[count];
            for (int i = 1; i < count + 1; i++) {
                fieldList[i - 1] = rsmd.getColumnName(i);// 把查询Sql语句中所涉及到的字段填充到List对象中
            }
            if (rs.next()) {// 遍历数据集
                for (int i = 0; i < fieldList.length; i++) {// 遍历某一行所有字段
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
     * 获取查询数据列表信息，List保存Map对象,Map对象的Key保存字段名称，Value保存字段值<br>
     * 只对Select查询语句有效,注意Map的key保存字段名称时记得要和查询语句中的字段名称相一致
     * 
     * @param sql 查询SQL语句
     * @return List 对象
     */
    public List getQueryListMap(String sql) {
        PreparedStatement ppst = null;
        ResultSet rs = null;
        List qryResultList = new ArrayList();
        try {
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
            }
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();// 获取数据集元数据信息
            if (log.isDebugEnabled())
                log.debug("获取数据集元数据信息(ResultSetMetaData实例),以便进一步获取各个字段信息-->" + rsmd.toString());
            int count = rsmd.getColumnCount();// 字段列数
            String[] fieldList = new String[count];
            for (int i = 1; i < count + 1; i++) {
                fieldList[i - 1] = rsmd.getColumnName(i);// 把查询Sql语句中所涉及到的字段填充到List对象中
            }
            if (log.isDebugEnabled()) {
                log.debug("各个字段信息:" + Arrays.asList(fieldList).toString());
            }
            while (rs.next()) {// 遍历数据集
                Map map = new HashMap();
                for (int i = 0; i < fieldList.length; i++) {// 遍历某一行所有字段
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
     * 根据查询SQL语句所得结果封装到一个JavaVo对象里面去并返回此对象<br>
     * 只取一行数据。如果sql返回多行数据则取第一行
     * 
     * @param sql 查询语句
     * @param cla Pojo类Class对象
     * @return Object 一个Vo对象,如果返回为null则该查询语句没有查询所得结果
     */
    public Object getQueryObj(String sql, Class cla) {
        return getQueryData(sql, cla, false);
    }

    /**
     * 根据SQL查询语句获取对象Vo的List
     * 
     * @param sql 查询语句
     * @param cla Pojo类Class对象
     * @return List 集合对象,List中保存各个Cla对象的实例
     */
    public List getQueryObjList(String sql, Class cla) {
        return (List) getQueryData(sql, cla, true);
    }

    /**
     * getQueryObj和getQueryObjList调用的取数方法<br>
     * 逻辑相近，为提高代码使用率.抽象出该方法
     * 
     * @param sql 查询语句
     * @param cla Pojo类Class对象
     * @param isQueryList
     *            true:由getQueryObjList方法调用,返回List,false:由getQueryObj方法调用,
     *            返回Object
     * @return Object getQueryObjList方法调用,返回List,如果为getQueryObj方法调用,返回Object
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
                log.info("执行SQL:" + sql);
            }
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();// 查询所得的总列数
            // 保存查询各个字段
            String[] colStr = new String[count];
            for (int i = 1; i <= count; i++) {
                colStr[i - 1] = rsmd.getColumnName(i);
            }
            // 所提供的POJO类的类路径
            String cacheKey = QUERY_CACHEKEY_LCL + cla.getName();
            // 如果缓存中没有缓存Bean反射信息
            if (!cache.contains(cacheKey)) {
                if (log.isDebugEnabled()) {
                    log.debug("没有对" + cla.getName() + "进行反射缓存!");
                }
                cacheFiller(cla);// 调用方法进行对bean反射缓存相关信息
            }
            if (cache.contains(cacheKey)) {
                if (log.isDebugEnabled()) {
                    log.debug("已经成功对" + cla.getName() + "进行反射缓存");
                    log.debug("现从缓存中获取" + cla.getName() + "的各个字段信息!!!");
                    Map beanCacheMap = (Map) cache.getProperty(cacheKey);
                    for (int i = 0; i < colStr.length; i++) {
                        CacheParam cp = (CacheParam) beanCacheMap.get(colStr[i]);
                        log.debug("查询字段:" + colStr[i] + "映射为---->" + cla.getName() + ":" + cp.getFieldName() + "("
                                + cp.getParamClassName() + ")");
                    }
                }
            } else {
                throw new RecRunTimeException("由于对" + cla.getName() + "缓存失败,后台不会继续进行查询!");
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
            log.debug("查询所得数据行数::" + resultList.size());
        }
        return isQueryList ? resultList : resultObj;
    }

    /**
     * 把SQL查询所得封装到一个对象数组中<br>
     * 注意:此SQL查询如果返回条记录，则对象数组封装了此记录<br>
     * 如果此SQL查询返回的是多行记录，则对象数组封装了第一行记录<br>
     * 如果查询所得没有记录，则返回null
     * 
     * @param sql SQL查许语句
     * @return Object[]
     */
    public Object[] getQueryObjArray(String sql) {
        Object[] dataObjs = null;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
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
                        log.debug("获取字段:" + rsmd.getColumnName(i + 1) + "的数据:[" + dataObjs[i] + "] >>>并注入到对象数组中,对应下标:"
                                + i);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("----->>把对象数组放进一个List容器中=" + Arrays.toString(dataObjs));
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
     * 获取sql查询涉及到的列的名称
     * 
     * @param sql String
     * @return List 包含各个字段的名称
     */
    public List getColumnNameInList(String sql) {
        List list = new ArrayList();
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
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
     * 把SQL查询所得封装到一个对象数组中，此数组用List保存<br>
     * 如果SQL查询所得没有记录，则返回的List集合对象isEmpty()为true
     * 
     * @param sql SQL查许语句
     * @return List
     */
    public List getQueryObjArrayInList(String sql) {
        List list = new ArrayList();
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            if (log.isInfoEnabled()) {
                log.info("执行SQL:" + sql);
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
                        log.debug("获取字段[" + rsmd.getColumnName(i + 1) + "]的数据:" + dataObjs[i] + " >>>并注入到对象数组中,对应下标:"
                                + i);
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("----->>把对象数组放进一个List容器中=" + Arrays.toString(dataObjs));
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

    // 查询缓存的cachekey前缀字符串
    private static final String QUERY_CACHEKEY_LCL = "SESSION_QUERY:";

    private void cacheFiller(Class cla) {
        GlobalCache cache = CacheManager.getGlobalCache();
        String cacheKey = QUERY_CACHEKEY_LCL + cla.getName();
        Map beanCacheMap = new HashMap();
        Field[] fields = cla.getDeclaredFields();
        if (log.isDebugEnabled()) {
            log.debug("即将对" + cla.getName() + "反射缓存相关信息:::");
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
                log.debug("对" + cla.getName() + "的字段" + field.getName() + "进行缓存...");
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
     * 内部方法,在利用JavaBean对象动态插入数据库记录的方法中调用<br>
     * 用于ResultSet中动态update列
     * 
     * @param rs ResultSet对象
     * @param fieldName字段名称
     * @param cla JavaBean对象的Class
     */
    private void setDataToResultSet(ResultSet rs, String fieldName, Object obj) {
        Method[] mets = obj.getClass().getDeclaredMethods();
        try {
            for (int i = 0; i < mets.length; i++) {
                Method met = mets[i];// 返回Method对象
                String metName = met.getName();
                if (("get" + fieldName).equalsIgnoreCase(metName)) {
                    String retTypeName = met.getReturnType().toString();// getter方法的返回类型
                    Object retValue = met.invoke(obj, new Object[] {});// 获取getter方法的返回值
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
     * 方法:executeBatchSql(String sql, Object[] objs, ParamCon pc)专用<br>
     * 内部调用
     * 
     * @param ppst PreparedStatement对象
     * @param object JavaBean对象
     * @param pc ParamCon对象
     */
    private boolean setPreparedStatementParam(PreparedStatement ppst, Object object, ParamCon pc, boolean isBatchExecute) {
        boolean isSucceed = true;
        try {
            for (int i = 0; i < pc.getParamLength(); i++) {
                int index = pc.getParamObject(i).getIndex();// 字段下标
                String methodName = "get" + StringUtil.initCap(pc.getParamObject(i).getTypeName());// 方法名称
                int paramType = pc.getParamObject(i).getParamType();// 数据类型
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
            }// 结束遍历
             // 如果是批处理
            if (isBatchExecute)
                ppst.addBatch();
        } catch (Exception e) {
            isSucceed = false;
            throw new RecRunTimeException(e);
        }
        return isSucceed;
    }

    /**
     * 打开一个数据库连接<br>
     * 所提供的Connection对象必须打开事务
     * 
     * @param conn Connection对象
     */
    public DBSessionImpl open(Connection conn) {
        if (log.isDebugEnabled()) {
            log.debug("把一个Connection连接对象关联到DBSession中");
        }
        this.conn = conn;
        // try {
        // if (this.conn.getAutoCommit()) {
        // this.conn.setAutoCommit(false);
        // if (log.isDebugEnabled()) {
        // log.debug("将DBSession内部所引用的Connection对象自动事务提交设置为false");
        // }
        // }
        // } catch (Exception e) {
        // throw new RecRunTimeException(e);
        // }
        return this;
    }

    /**
     * 设置是否自动提交事务
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
     * 提交事务<br>
     * 即调用内部引用的Connection.commit();
     */
    public void commit() {
        try {
            if (log.isDebugEnabled()) {
                log.debug("--->>>>提交DBSession事务...");
            }
            this.conn.commit();
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        }
    }

    /**
     * 关闭数据库连接资源
     */
    public void close() {
        try {
            if (this.conn != null && !this.conn.isClosed()) {
                this.conn.close();
            }
            if (log.isDebugEnabled()) {
                log.debug("释放DBSession的Connection资源...");
            }
            this.conn = null;
        } catch (SQLException e) {
            throw new RecRunTimeException(e.getMessage(), e);
        }
    }

    /**
     * 提交事务并关闭
     */
    public void commitAndClose() {
        commit();
        close();
    }

}
