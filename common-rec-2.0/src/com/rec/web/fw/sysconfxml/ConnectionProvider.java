package com.rec.web.fw.sysconfxml;

/**
 * SystemConfig.xml中的third-connection-provider接点
 * 
 * @author recolar
 * 
 * @see
 * @version
 * @since
 */
public class ConnectionProvider {

	/**
	 * 是否能用
	 */
	private boolean enable;

	/**
	 * 类路径,此类必须实现接口IConnectionProvider
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
