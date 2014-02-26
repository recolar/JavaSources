package com.rec.web.fw.sysconfxml;

/**
 * SystemConfig.xml�е�third-connection-provider�ӵ�
 * 
 * @author recolar
 * 
 * @see
 * @version
 * @since
 */
public class ConnectionProvider {

	/**
	 * �Ƿ�����
	 */
	private boolean enable;

	/**
	 * ��·��,�������ʵ�ֽӿ�IConnectionProvider
	 */
	private String classPath;

	/**
	 * @return the classPath
	 */
	public String getClassPath() {
		return classPath;
	}

	/**
	 * @param classPath the classPath to set
	 */
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}
}
