package com.rec.web.fw;

import java.util.Properties;

/**
 * 代替Log4J配置文件的类，封装了Log4J的配置信息<BR>
 * Log4J有4个输出级别,从上到下是ERROR,WARN,INFO,DEBUG<BR>
 * 如果定义了INFO级别则INFO及以上级别都会输出
 * 
 * @author Recolar
 * @version 1.0.0 2007-1-6
 * @since JDK1.5.0
 */
public class Log4jProp {

	/**
	 * 设置rootLogger属性
	 */
	private String rootLogger;

	/**
	 * 设置日志文件输出的完整路径
	 */
	private String logFilePath;

	/**
	 * 设置日志文件的最大容量
	 */
	private int maxFileSize;

	/**
	 * 设置日志文件的备份文件数
	 */
	private int maxBackupIndex;

	/**
	 * 内部引用一个Preperties实例，此实例是提供Log4J配置信息的基础
	 */
	private Properties prop = new Properties();

	/**
	 * 设置日志文件输出的完整路径,如果Logger中没有提供参数需要设置输出logFile,则此设置无效
	 * 
	 * @param logFilePath 完整路径,如:"C:/a/b.log"
	 * @return Log4jProp实例
	 */
	public Log4jProp setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		return this;
	}

	/**
	 * 设置最大输出日志的备份数量
	 * 
	 * @param maxBackupIndex int 备份数量
	 * @return Log4jProp实例
	 */
	public Log4jProp setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
		return this;
	}

	/**
	 * 工厂方法构造Log4jProp实例
	 * 
	 * @return Log4jProp
	 */
	public static Log4jProp getInstance() {
		return new Log4jProp();
	}

	/**
	 * 设置日志文件的最大容量(单位:KB)
	 * 
	 * @param maxFileSize
	 */
	public Log4jProp setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
		return this;
	}

	/**
	 * 设置RootLogger属性,可以使用LoggerParam中的各个常量来进行设置
	 * 
	 * @param rootLogger String
	 * @return
	 */
	public Log4jProp setRootLogger(String rootLogger) {
		this.rootLogger = rootLogger;
		return this;
	}

	/**
	 * 获取Preperties实例
	 * 
	 * @return Preperties
	 */
	public Properties getLog4jProperties() {
		if (this.rootLogger == null && this.logFilePath == null && this.maxFileSize <= 0 && this.maxBackupIndex < 1) {
			loadDefault();
		} else {
			loadBySetter();
		}
		return this.prop;
	}

	/** 日志输出文本格式 */
	private static final String LOG_LAYOUT = "%d{yyyy-MM-dd HH:mm:ss,SSS} [%F:%L] %p:%m%n";

	/**
	 * 加载所设置了的属性
	 * 
	 */
	private void loadBySetter() {
		if (this.rootLogger == null) {
			// prop.setProperty("log4j.rootLogger", "DEBUG, stdout,logfile");//
			// 输出到日志
			prop.setProperty("log4j.rootLogger", "DEBUG, stdout");
		} else {
			prop.setProperty("log4j.rootLogger", this.rootLogger);
		}
		if (this.logFilePath == null) {
			prop.setProperty("log4j.appender.logfile.File", "c:/webproject.log");
		} else {
			prop.setProperty("log4j.appender.logfile.File", this.logFilePath);
		}
		if (this.maxFileSize <= 0) {
			prop.setProperty("log4j.appender.logfile.MaxFileSize", "512KB");
		} else {
			prop.setProperty("log4j.appender.logfile.MaxFileSize", this.maxFileSize + "KB");
		}
		if (this.maxBackupIndex < 1) {
			prop.setProperty("log4j.appender.logfile.MaxBackupIndex", "3");
		} else {
			prop.setProperty("log4j.appender.logfile.MaxBackupIndex", String.valueOf(this.maxBackupIndex));
		}
		prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", LOG_LAYOUT);
		prop.setProperty("log4j.appender.logfile", "org.apache.log4j.RollingFileAppender");
		prop.setProperty("log4j.appender.logfile.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.logfile.layout.ConversionPattern", LOG_LAYOUT);
	}

	/**
	 * 默认加载<br>
	 * 默认信息:1.不输出到日志 2.默认级别为Debug或者以上
	 * 
	 */
	public Log4jProp loadDefault() {
		prop.setProperty("log4j.rootLogger", "DEBUG, stdout");// 不输出到日志
		prop.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.stdout.layout.ConversionPattern", LOG_LAYOUT);

		prop.setProperty("log4j.appender.logfile", "org.apache.log4j.RollingFileAppender");
		prop.setProperty("log4j.appender.logfile.File", "c:/webproject.log");
		prop.setProperty("log4j.appender.logfile.MaxFileSize", "512KB");
		prop.setProperty("log4j.appender.logfile.MaxBackupIndex", "3");
		prop.setProperty("log4j.appender.logfile.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.logfile.layout.ConversionPattern", LOG_LAYOUT);
		return this;
	}

}
