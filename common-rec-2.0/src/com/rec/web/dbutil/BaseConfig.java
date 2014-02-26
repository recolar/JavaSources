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
	 * 数据库是否自动提交
	 */
	public static boolean CONN_AUTOCOMMIT = false;

	/** ******************************************************************************** */
	/**
	 * 登录数据库的URL
	 */
	private static String LOGIN_URL = null;

	/**
	 * 登录数据库用户名
	 */
	private static String LOGIN_USERNAME = null;

	/**
	 * 当前登陆数据库名称
	 */
	private static String DB_NAME = null;

	/**
	 * 数据库版本
	 */
	private static String DB_VERSION = null;

	/**
	 * 数据库驱动名称
	 */
	private static String DB_DRIVERNAME = null;

	/**
	 * 数据库驱动版本
	 */
	private static String DB_DRIVERVERSION = null;

	/**
	 * 数据库名字
	 */
	private static String DB_PRODUCTNAME = null;

	/**
	 * 数据库的版本
	 */
	private static String DB_PRODUCTVERSION = null;

	/**
	 * 当前所使用数据库标识
	 */
	private static int DATABASE_TYPE;

	/**
	 * 是否支持事务
	 */
	private static boolean DB_SUPPORT_TRANSACTION;

	/**
	 * 第三方数据源ConnectionProvider路径
	 */
	public static String CONNECTION_PROVIDER_CLASSPATH;

	/** ******************************************************************************** */

	/**
	 * oracle数据库名称
	 */
	public static final String DB_ORACLE_PRODUCTNAME = "Oracle";

	/**
	 * Sql Server数据库名称
	 */
	public static final String DB_SQL_PRODUCTNAME = "Microsoft SQL Server";

	/**
	 * MySql数据库名称
	 */
	public static final String DB_MYSQL_PRODUCTNAME = "MySQL";

	/**
	 * 当前使用的数据库标识:Oracle
	 */
	public static final int DATABASE_ORACLE = 1;

	/**
	 * 当前使用的数据库标识:SQLSERVER
	 */
	public static final int DATABASE_SQLSERVER = 2;

	/**
	 * 当前使用的数据库标识:MYSQL
	 */
	public static final int DATABASE_MYSQL = 3;

	/**
	 * 获取登录数据库的url信息
	 * 
	 * @return
	 */
	public static String getLOGIN_URL() {
		return LOGIN_URL;
	}

	/**
	 * 获取登录数据库的用户名
	 */
	public static String getLOGIN_USERNAME() {
		return LOGIN_USERNAME;
	}

	public static int getDATABASE_TYPE() {
		return DATABASE_TYPE;
	}

	/**
	 * 获取连接数据库的产品名称
	 */
	public static String getCONN_DATABASENAME() {
		return DB_NAME;
	}

	/**
	 * 获取数据库的版本信息
	 */
	public static String getDATABASE_VERSION() {
		return DB_VERSION;
	}

	/**
	 * 获取数据库的驱动名称
	 */
	public static String getDATABASE_DRIVERNAME() {
		return DB_DRIVERNAME;
	}

	/**
	 * 获取数据库的名字
	 */
	public static String getPRODUCT_NAME() {
		return DB_PRODUCTNAME;
	}

	/**
	 * 获取数据库的版本
	 */
	public static String getPRODUCT_VERSION() {
		return DB_PRODUCTVERSION;
	}

	/**
	 * 获取数据库的驱动版本
	 */
	public static String getDRIVER_VERSION() {
		return DB_DRIVERVERSION;
	}

	public static boolean isSUPPORTTRANSACTION() {
		return DB_SUPPORT_TRANSACTION;
	}

	public static final int CONN_BY_RECCONNPOOL = 1;// 自定义的连接池

	public static final int CONN_BY_THIRD_CONNECTION = 2;// 第三方数据源插件

	private static int CONNECTION_PATTERN = 1;// 数据库的连接标识(默认为配置成自定义的连接池)

	private static boolean HASCONFIGED_CONNECTION_PATTERN = false;// 是否已经配置过了的标志

	/**
	 * 配置数据库连接池的连接方式<br>
	 * 目前只提供两种连接方式:1.自定义的连接池 2:第三方数据源插件
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
	 * 获取数据库连接池模式的标识
	 * 
	 * @return int
	 */
	public static int getCONNECTION_PATTERN() {
		return CONNECTION_PATTERN;
	}

	/** ********************************************************************* */
	/**
	 * 数据库链接驱动
	 */
	private static String CONN_DRIVERNAME = "";

	/**
	 * 数据库连接字符串
	 */
	private static String CONN_URL = "";

	/**
	 * 数据库连接用户名
	 */
	private static String CONN_USERNAME = "";

	/**
	 * 数据库连接密码
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
	 * 获取数据库驱动名称
	 * 
	 * @return String 数据库驱动
	 */
	public static String getCONN_DRIVERNAME() {
		return CONN_DRIVERNAME;
	}

	/**
	 * 获取数据库连接URL字符串
	 * 
	 * @return String URL
	 */
	public static String getCONN_URL() {
		return CONN_URL;
	}

	/**
	 * 获取数据库连接用户名
	 * 
	 * @return String userName
	 */
	public static String getCONN_USERNAME() {
		return CONN_USERNAME;
	}

	/**
	 * 获取数据库连接密码
	 * 
	 * @return String Password
	 */
	public static String getCONN_PASSWORD() {
		return CONN_PASSWORD;
	}
	/** ********************************************************************* */
}
