package com.rec.web.dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rec.exception.RecRunTimeException;

/**
 * 用于连接DB的工具类<br>
 * 
 * @author Recolar
 * @version 1.0.1 2006-12-30
 * @since JDK1.5.0
 */
public final class ConnUtil {

	private ConnUtil() {
	}

	/**
	 * 关闭数据库连接的相关资源<br>
	 * 
	 * @param rs,ppst,conn
	 * @return 关闭成功返回true否则返回false
	 */
	public static boolean releaseConnResource(final ResultSet resultSet, final Statement ppst, final Connection conn) {
		boolean succeed = false;
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (ppst != null) {
				ppst.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			succeed = true;
		} catch (SQLException e) {
			throw new RecRunTimeException(e);
		}
		return succeed;
	}

	/**
	 * 关闭数据库连接的相关资源<br>
	 * 
	 * @param rs,ppst
	 * @return 关闭成功返回true否则返回false
	 */
	public static boolean releaseConnResource(final ResultSet resultSet, final Statement ppst) {
		boolean succeed = false;
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (ppst != null) {
				ppst.close();
			}
			succeed = true;
		} catch (SQLException e) {
			throw new RecRunTimeException(e);
		}
		return succeed;
	}

	/**
	 * 关闭数据库连接的相关资源<br>
	 * 
	 * @param conn
	 * @return 关闭成功返回true否则返回false
	 */
	public static boolean releaseConnResource(final Connection conn) {
		boolean succeed = false;
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			succeed = true;
		} catch (SQLException e) {
			throw new RecRunTimeException(e);
		}
		return succeed;
	}

	/**
	 * 关闭数据库连接的相关资源<br>
	 * 
	 * @param st,conn
	 * @return 关闭成功返回true否则返回false
	 */

	public static boolean releaseConnResource(final Statement ppst, final Connection conn) {
		boolean succeed = false;
		try {
			if (ppst != null) {
				ppst.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			succeed = true;
		} catch (SQLException err) {
			throw new RecRunTimeException(err);
		}
		return succeed;
	}

	/**
	 * 关闭数据库连接的相关资源<br>
	 * 
	 * @param st
	 * @return 关闭成功返回true否则返回false
	 */
	public static boolean releaseConnResource(final Statement ppst) {
		boolean succeed = false;
		try {
			if (ppst != null) {
				ppst.close();
			}
			succeed = true;
		} catch (SQLException err) {
			throw new RecRunTimeException(err);
		}
		return succeed;
	}
}
