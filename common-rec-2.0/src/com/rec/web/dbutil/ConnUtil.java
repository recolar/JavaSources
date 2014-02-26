package com.rec.web.dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rec.exception.RecRunTimeException;

/**
 * ��������DB�Ĺ�����<br>
 * 
 * @author Recolar
 * @version 1.0.1 2006-12-30
 * @since JDK1.5.0
 */
public final class ConnUtil {

	private ConnUtil() {
	}

	/**
	 * �ر����ݿ����ӵ������Դ<br>
	 * 
	 * @param rs,ppst,conn
	 * @return �رճɹ�����true���򷵻�false
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
	 * �ر����ݿ����ӵ������Դ<br>
	 * 
	 * @param rs,ppst
	 * @return �رճɹ�����true���򷵻�false
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
	 * �ر����ݿ����ӵ������Դ<br>
	 * 
	 * @param conn
	 * @return �رճɹ�����true���򷵻�false
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
	 * �ر����ݿ����ӵ������Դ<br>
	 * 
	 * @param st,conn
	 * @return �رճɹ�����true���򷵻�false
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
	 * �ر����ݿ����ӵ������Դ<br>
	 * 
	 * @param st
	 * @return �رճɹ�����true���򷵻�false
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
