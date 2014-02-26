package com.rec.web.dbutil;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * DBSession������
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
	 * ��ȡһ��DBSessionʵ��
	 * 
	 * @return DBSession
	 */
	public static DBSession buildDBSession() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("��ȡһ������Connection��Դ��DBSessionʵ��");
		}
		return new DBSessionImpl();
	}

	/**
	 * ��ȡһ��DBSessionʵ��
	 * 
	 * @param conn Connection���ݿ����Ӷ���
	 * @return ������������Դ��DBSession����
	 */
	public static DBSession buildDBSession(final Connection conn) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("��ȡһ����Connection��Դ��DBSessionʵ��");
		}
		return new DBSessionImpl(conn);
	}
}
