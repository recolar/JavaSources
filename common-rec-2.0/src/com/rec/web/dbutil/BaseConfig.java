package com.rec.web.dbutil;


/**
 * @author Recolar
 * @version 1.0.0 2006-11-11
 * @since JDK1.5.0
 */
public final class BaseConfig {

	public static final String SQLSERVER_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";

	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

	private BaseConfig() {
	}

	/**
	 * ���ݿ��Ƿ��Զ��ύ
	 */
	public static boolean CONN_AUTOCOMMIT = false;

	/** ******************************************************************************** */
	/**
	 * ��¼���ݿ��URL
	 */
	private static String LOGIN_URL = null;

	/**
	 * ��¼���ݿ��û���
	 */
	private static String LOGIN_USERNAME = null;

	/**
	 * ��ǰ��½���ݿ�����
	 */
	private static String DB_NAME = null;

	/**
	 * ���ݿ�汾
	 */
	private static String DB_VERSION = null;

	/**
	 * ���ݿ���������
	 */
	private static String DB_DRIVERNAME = null;

	/**
	 * ���ݿ������汾
	 */
	private static String DB_DRIVERVERSION = null;

	/**
	 * ���ݿ�����
	 */
	private static String DB_PRODUCTNAME = null;

	/**
	 * ���ݿ�İ汾
	 */
	private static String DB_PRODUCTVERSION = null;

	/**
	 * ��ǰ��ʹ�����ݿ��ʶ
	 */
	private static int DATABASE_TYPE;

	/**
	 * �Ƿ�֧������
	 */
	private static boolean DB_SUPPORT_TRANSACTION;

	/**
	 * ����������ԴConnectionProvider·��
	 */
	public static String CONNECTION_PROVIDER_CLASSPATH;

	/** ******************************************************************************** */

	/**
	 * oracle���ݿ�����
	 */
	public static final String DB_ORACLE_PRODUCTNAME = "Oracle";

	/**
	 * Sql Server���ݿ�����
	 */
	public static final String DB_SQL_PRODUCTNAME = "Microsoft SQL Server";

	/**
	 * MySql���ݿ�����
	 */
	public static final String DB_MYSQL_PRODUCTNAME = "MySQL";

	/**
	 * ��ǰʹ�õ����ݿ��ʶ:Oracle
	 */
	public static final int DATABASE_ORACLE = 1;

	/**
	 * ��ǰʹ�õ����ݿ��ʶ:SQLSERVER
	 */
	public static final int DATABASE_SQLSERVER = 2;

	/**
	 * ��ǰʹ�õ����ݿ��ʶ:MYSQL
	 */
	public static final int DATABASE_MYSQL = 3;

	/**
	 * ��ȡ��¼���ݿ��url��Ϣ
	 * 
	 * @return
	 */
	public static String getLOGIN_URL() {
		return LOGIN_URL;
	}

	/**
	 * ��ȡ��¼���ݿ���û���
	 */
	public static String getLOGIN_USERNAME() {
		return LOGIN_USERNAME;
	}

	public static int getDATABASE_TYPE() {
		return DATABASE_TYPE;
	}

	/**
	 * ��ȡ�������ݿ�Ĳ�Ʒ����
	 */
	public static String getCONN_DATABASENAME() {
		return DB_NAME;
	}

	/**
	 * ��ȡ���ݿ�İ汾��Ϣ
	 */
	public static String getDATABASE_VERSION() {
		return DB_VERSION;
	}

	/**
	 * ��ȡ���ݿ����������
	 */
	public static String getDATABASE_DRIVERNAME() {
		return DB_DRIVERNAME;
	}

	/**
	 * ��ȡ���ݿ������
	 */
	public static String getPRODUCT_NAME() {
		return DB_PRODUCTNAME;
	}

	/**
	 * ��ȡ���ݿ�İ汾
	 */
	public static String getPRODUCT_VERSION() {
		return DB_PRODUCTVERSION;
	}

	/**
	 * ��ȡ���ݿ�������汾
	 */
	public static String getDRIVER_VERSION() {
		return DB_DRIVERVERSION;
	}

	public static boolean isSUPPORTTRANSACTION() {
		return DB_SUPPORT_TRANSACTION;
	}

	public static final int CONN_BY_RECCONNPOOL = 1;// �Զ�������ӳ�

	public static final int CONN_BY_THIRD_CONNECTION = 2;// ����������Դ���

	private static int CONNECTION_PATTERN = 1;// ���ݿ�����ӱ�ʶ(Ĭ��Ϊ���ó��Զ�������ӳ�)

	private static boolean HASCONFIGED_CONNECTION_PATTERN = false;// �Ƿ��Ѿ����ù��˵ı�־

	/**
	 * �������ݿ����ӳص����ӷ�ʽ<br>
	 * Ŀǰֻ�ṩ�������ӷ�ʽ:1.�Զ�������ӳ� 2:����������Դ���
	 * 
	 * @param connectionFlag
	 */
	public static void setCONNECTION_PATTERN(int connectionFlag) {
		if (!HASCONFIGED_CONNECTION_PATTERN) {
			if (connectionFlag == CONN_BY_RECCONNPOOL) {
				CONNECTION_PATTERN = CONN_BY_RECCONNPOOL;
			} else if (connectionFlag == CONN_BY_THIRD_CONNECTION) {
				CONNECTION_PATTERN = CONN_BY_THIRD_CONNECTION;
			}
			HASCONFIGED_CONNECTION_PATTERN = true;
		}
	}

	/**
	 * ��ȡ���ݿ����ӳ�ģʽ�ı�ʶ
	 * 
	 * @return int
	 */
	public static int getCONNECTION_PATTERN() {
		return CONNECTION_PATTERN;
	}

	/** ********************************************************************* */
	/**
	 * ���ݿ���������
	 */
	private static String CONN_DRIVERNAME = "";

	/**
	 * ���ݿ������ַ���
	 */
	private static String CONN_URL = "";

	/**
	 * ���ݿ������û���
	 */
	private static String CONN_USERNAME = "";

	/**
	 * ���ݿ���������
	 */
	private static String CONN_PASSWORD = "";

	private static boolean hasLoadConnResource = false;

	public static void setCONN_RESOURCE(String driverName, String url, String userName, String password) {
		if (!hasLoadConnResource) {
			CONN_DRIVERNAME = driverName;
			CONN_URL = url;
			CONN_USERNAME = userName;
			CONN_PASSWORD = password;
			hasLoadConnResource = true;
		}
	}

	/**
	 * ��ȡ���ݿ���������
	 * 
	 * @return String ���ݿ�����
	 */
	public static String getCONN_DRIVERNAME() {
		return CONN_DRIVERNAME;
	}

	/**
	 * ��ȡ���ݿ�����URL�ַ���
	 * 
	 * @return String URL
	 */
	public static String getCONN_URL() {
		return CONN_URL;
	}

	/**
	 * ��ȡ���ݿ������û���
	 * 
	 * @return String userName
	 */
	public static String getCONN_USERNAME() {
		return CONN_USERNAME;
	}

	/**
	 * ��ȡ���ݿ���������
	 * 
	 * @return String Password
	 */
	public static String getCONN_PASSWORD() {
		return CONN_PASSWORD;
	}
	/** ********************************************************************* */
}
