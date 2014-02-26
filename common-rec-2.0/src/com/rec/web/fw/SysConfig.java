package com.rec.web.fw;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * 用于在Web工程中初始化底层的门户类,提供方法来启动数据库的连接信息以及对Log4J信息的配置
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
	 * 根据Log4JProp配置类加载系统启动信息
	 * 
	 * @param logProp Log4JProp对象
	 */
	public static void loadLog4jInfo(Log4jProp logProp) {
		PropertyConfigurator.configure(logProp.getLog4jProperties());
		Log log = LogFactory.getLog(SysConfig.class);
		if (log.isDebugEnabled()) {
			log.debug("读取Log4J配置成功......");
		}
	}

	/**
	 * 根据制定的Log4J配置路径加载系统启动信息<br>
	 * 该文件路径所指向文件可以是properties文件或者是xml文件的配置信息
	 * 
	 * @param logProp Log4JProp对象
	 */
	public static void loadLog4jConfigFile(String configFilePath) {
		PropertyConfigurator.configure(configFilePath);
		Log log = LogFactory.getLog(SysConfig.class);
		if (log.isDebugEnabled()) {
			log.debug("读取Log4J配置成功......");
		}
	}

	/**
	 * 屏蔽log4J的输出功能
	 */
	public static void setLog4jDisable() {
		Log4jProp logProp = new Log4jProp();
		logProp.setRootLogger(LoggerParam.LOGGER_OFF);
		PropertyConfigurator.configure(logProp.getLog4jProperties());
	}

}
