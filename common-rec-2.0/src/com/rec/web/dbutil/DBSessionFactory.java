package com.rec.web.dbutil;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * DBSession工厂类
 * 
 * @author liangxf
 * 
 * @see DBSession,DBSessionImpl
 * @version 1.0.0 2007-11-05
 * @since JDK1.5
 */
public final class DBSessionFactory {

	private static final Log LOG = LogFactory.getLog(DBSessionFactory.class);

	private DBSessionFactory() {
	}

	/**
	 * 获取一个DBSession实例
	 * 
	 * @return DBSession
	 */
	public static DBSession buildDBSession() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("获取一个不带Connection资源的DBSession实例");
		}
		return new DBSessionImpl();
	}

	/**
	 * 获取一个DBSession实例
	 * 
	 * @param conn Connection数据库连接对象
	 * @return 包含了连接资源的DBSession对象
	 */
	public static DBSession buildDBSession(final Connection conn) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("获取一个带Connection资源的DBSession实例");
		}
		return new DBSessionImpl(conn);
	}
}
