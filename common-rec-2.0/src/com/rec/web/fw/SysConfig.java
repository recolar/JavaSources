package com.rec.web.fw;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * ������Web�����г�ʼ���ײ���Ż���,�ṩ�������������ݿ��������Ϣ�Լ���Log4J��Ϣ������
 * 
 * @author Recolar
 * @version 1.0.0 2007-1-6
 * @since JDK1.5.0
 * 
 */
public class SysConfig {

	private SysConfig() {
	}

	/**
	 * ����Log4JProp���������ϵͳ������Ϣ
	 * 
	 * @param logProp Log4JProp����
	 */
	public static void loadLog4jInfo(Log4jProp logProp) {
		PropertyConfigurator.configure(logProp.getLog4jProperties());
		Log log = LogFactory.getLog(SysConfig.class);
		if (log.isDebugEnabled()) {
			log.debug("��ȡLog4J���óɹ�......");
		}
	}

	/**
	 * �����ƶ���Log4J����·������ϵͳ������Ϣ<br>
	 * ���ļ�·����ָ���ļ�������properties�ļ�������xml�ļ���������Ϣ
	 * 
	 * @param logProp Log4JProp����
	 */
	public static void loadLog4jConfigFile(String configFilePath) {
		PropertyConfigurator.configure(configFilePath);
		Log log = LogFactory.getLog(SysConfig.class);
		if (log.isDebugEnabled()) {
			log.debug("��ȡLog4J���óɹ�......");
		}
	}

	/**
	 * ����log4J���������
	 */
	public static void setLog4jDisable() {
		Log4jProp logProp = new Log4jProp();
		logProp.setRootLogger(LoggerParam.LOGGER_OFF);
		PropertyConfigurator.configure(logProp.getLog4jProperties());
	}

}
