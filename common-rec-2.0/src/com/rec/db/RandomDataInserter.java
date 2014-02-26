/**
 * ssl.
 */
package com.rec.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.rec.service.Reflector;
import com.rec.service.StringUtil;

/**
 * 插入随机数据的工具类.
 * 
 * @author recolar
 * @version 1.0.0
 * @since JDK1.6.0
 * 
 */
public class RandomDataInserter {

    /** LOG. */
    private static final Log LOG = LogFactory.getLog(RandomDataInserter.class);

    /** 声明数据库连接对象. */
    private Connection conn;

    /**
     * @return the conn
     */
    public final Connection getConn() {
        return conn;
    }

    /**
     * @param conn the conn to set
     */
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * 
     */
    public RandomDataInserter() {
    }

    /**
     * 
     * @param conn c
     */
    public RandomDataInserter(final Connection conn) {
        this.conn = conn;
    }

    /**
     * 根据数据表名,返回该数据表所有字段名称的集合.
     * 
     * @param tableName 数据表名
     * @param columnNames 列名称集合
     * @param columnSizes 列长度集合
     */
    private void fillColumnInfoList(final String tableName,
            final List<String> columnNames, final List<Integer> columnSizes) {
        final String sql = "SELECT * FROM " + tableName;
        PreparedStatement ppst = null;
        ResultSet rs = null;
        try {
            ppst = conn.prepareStatement(sql);
            rs = ppst.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                columnNames.add(rsmd.getColumnName(i).toUpperCase());
                if (columnSizes != null) {
                    columnSizes.add(rsmd.getColumnDisplaySize(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeResource(rs, ppst, null);
        }
    }

    /**
     * 将指定的objs对象数组，插入指定名称的数据表中.
     * 
     * @param tableName 数据表名称
     * @param vos 集合列表
     * @param isDeleteFirst 是否先删除数据表所有数据
     */
    public void insertData(final String tableName, final List<?> vos,
            final boolean isDeleteFirst) {
        Assert.notNull(conn, "Connection对象没有设置。。。");
        Assert.notEmpty(vos, "vos集合参数个数为0。。。");

        final Class<?> voClazz = vos.get(0).getClass();
        final Field[] fields = voClazz.getDeclaredFields();
        final Method[] getterMethods = Reflector.getGetterMethodsByFileds(
                fields, voClazz);
        final String insertSQL = "INSERT INTO " + tableName + " VALUES("
                + StringUtil.joinStrBySize("?", ",", fields.length) + ")";

        final List<String> columnNames = new ArrayList<String>();
        fillColumnInfoList(tableName, columnNames, null);
        PreparedStatement ppst = null;
        try {
            if (isDeleteFirst) {
                final String deleteSQL = "TRUNCATE TABLE " + tableName;
                ppst = conn.prepareStatement(deleteSQL);
                ppst.executeUpdate();
                if (LOG.isDebugEnabled()) {
                    LOG.debug("删除数据表所有数据-->>执行SQL：：" + deleteSQL);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("执行SQL:" + insertSQL);
            }
            ppst = conn.prepareStatement(insertSQL);
            for (Object vo : vos) {
                for (int i = 0, len = columnNames.size(); i < len; i++) {
                    final String columnName = columnNames.get(i);
                    Object value = null;
                    for (int k = 0; k < fields.length; k++) {
                        if (columnName.equalsIgnoreCase(fields[k].getName())) {
                            Method getterMethod = getterMethods[k];
                            Throwable ex = null;
                            try {
                                value = getterMethod
                                        .invoke(vo, new Object[] {});
                            } catch (SecurityException e) {
                                ex = e;
                            } catch (IllegalArgumentException e) {
                                ex = e;
                            } catch (IllegalAccessException e) {
                                ex = e;
                            } catch (InvocationTargetException e) {
                                ex = e;
                            }
                            if (ex != null) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    ppst.setObject(i + 1, value);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("插入数据：：：" + vo);
                }
                ppst.addBatch();
            }
            ppst.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e1);
            }
            throw new RuntimeException(e);
        } finally {
            DBUtil.closeConnection(conn);
            DBUtil.closeStatement(ppst);
        }
    }
}
