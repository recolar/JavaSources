package com.rec.web.fw.sysconfxml;

import java.util.Properties;

/**
 * systemconfig.xml�е�log4j�ڵ�
 * 
 * @author recolar
 * 
 */
public class Log4J {

	private boolean isEnable;// �Ƿ����ÿ���

	private Properties configParam = new Properties();// ���ò���

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
