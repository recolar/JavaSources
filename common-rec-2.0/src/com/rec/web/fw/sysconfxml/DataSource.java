package com.rec.web.fw.sysconfxml;

/**
 * systemconfig.xml�е�datasource�ڵ�
 * 
 * @author recolar
 * 
 */
public class DataSource {

	// ���ݿ�ϵͳ�����õ����ݿ����ӳصķ�ʽ 1:�Զ������ӳ� ���������������ʹ�ø���serverType��������ݿ����ӳ�
	private int connectionPattern = -1;

	// �������ݿ�URL
	private String connectionURL;

	// �������ݿ����������
	private String driverClass;

	// �������ݿ�ĵ�¼�û���
	private String userName;

	// �������ݿ�ĵ�¼����
	private String password;

	// ���ݿ�����������
	private int maxConnection = -1;

	// ÿ�����ݿ����ӵ����ȴ�ʱ��
	private int maxWaittingTime = -1;

	// Connection�����Ƿ��Զ��ύ
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