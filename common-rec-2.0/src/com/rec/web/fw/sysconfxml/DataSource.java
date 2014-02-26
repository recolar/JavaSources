package com.rec.web.fw.sysconfxml;

/**
 * systemconfig.xml中的datasource节点
 * 
 * @author recolar
 * 
 */
public class DataSource {

	// 数据库系统所采用的数据库连接池的方式 1:自定义连接池 如果是其他数字则使用根据serverType定义的数据库连接池
	private int connectionPattern = -1;

	// 连接数据库URL
	private String connectionURL;

	// 连接数据库的驱动名称
	private String driverClass;

	// 连接数据库的登录用户名
	private String userName;

	// 连接数据库的登录密码
	private String password;

	// 数据库的最大连接数
	private int maxConnection = -1;

	// 每个数据库连接的最大等待时间
	private int maxWaittingTime = -1;

	// Connection事物是否自动提交
	private boolean autoCommit;

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(final boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(final String connectionURL) {
		this.connectionURL = connectionURL;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(final String driverClass) {
		this.driverClass = driverClass;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(final int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public int getMaxWaittingTime() {
		return maxWaittingTime;
	}

	public void setMaxWaittingTime(final int maxWaittingTime) {
		this.maxWaittingTime = maxWaittingTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

	public int getConnectionPattern() {
		return connectionPattern;
	}

	public void setConnectionPattern(final int connectionPattern) {
		this.connectionPattern = connectionPattern;
	}

}