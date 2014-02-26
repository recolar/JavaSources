package com.rec.web.fw.sysconfxml;

import java.util.Properties;

/**
 * systemconfig.xml中的log4j节点
 * 
 * @author recolar
 * 
 */
public class Log4J {

	private boolean isEnable;// 是否配置可用

	private Properties configParam = new Properties();// 配置参数

	public Properties getConfigParam() {
		return configParam;
	}

	public void setConfigParam(Properties configParam) {
		this.configParam = configParam;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean enable) {
		this.isEnable = enable;
	}
}
