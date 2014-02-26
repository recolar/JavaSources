package com.rec.web.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据集生产类<br>
 * 
 * @author Recolar
 * @version 1.0.0 2006-11-11
 * @since JDK1.5.0
 * 
 */
public class DSUtil {

	private ResultSet resultSet;

	private PreparedStatement ppst;

	private Connection conn;

	/**
	 * 构造数据集工厂类的对象
	 * 
	 * @param conn 数据库连接对象
	 * @param ppst PreparedStatement 对象
	 * @param ResultSet 数据集对象
	 */
	public DSUtil(Connection conn, PreparedStatement ppst, ResultSet rs) {
		this.conn = conn;
		this.ppst = ppst;
		this.resultSet = rs;
	}

	/**
	 * @return the resultSet
	 */
	public final ResultSet getResultSet() {
		return resultSet;
	}

	/**
	 * @param resultSet the resultSet to set
	 */
	public final void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * @return the ppst
	 */
	public final PreparedStatement getPpst() {
		return ppst;
	}

	/**
	 * @param ppst the ppst to set
	 */
	public final void setPpst(PreparedStatement ppst) {
		this.ppst = ppst;
	}

	/**
	 * @return the conn
	 */
	public final Connection getConn() {
		return conn;
	}

	/**
	 * @param conn the conn to set
	 */
	public final void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 关闭数据集的相关的资源<br>
	 */
	public void closeDSResource() {
		try {
			if (this.resultSet != null) {
				resultSet.close();
			}
			if (this.ppst != null) {
				ppst.close();
			}
			if (!this.conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭除了Connection之外的资源
	 * 
	 */
	public void closeDSResourceWithoutConnection() {
		try {
			if (this.resultSet != null) {
				resultSet.close();
			}
			if (this.ppst != null) {
				ppst.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据集的相关的资源<br>
	 * 
	 * @param rs ResultSet对象
	 */
	public void closeDSResource(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (this.ppst != null) {
				ppst.close();
			}
			if (!this.conn.isClosed())
				conn.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

}
