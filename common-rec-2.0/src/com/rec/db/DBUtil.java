package com.rec.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.jdbc.support.JdbcUtils;

/**
 * 数据库工具类.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-25
 * @since JDK1.6.0
 * 
 */
public class DBUtil extends JdbcUtils {

	/**
	 * 关闭数据库相关资源.
	 * 
	 * @param rs ResultSet
	 * @param ppst Statement
	 * @param conn Connection
	 */
	public static void closeResource(ResultSet rs, PreparedStatement ppst, Connection conn) {
		closeResultSet(rs);
		closeStatement(ppst);
		closeConnection(conn);
	}

}
