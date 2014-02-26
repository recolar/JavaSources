package com.rec.web.fw;

import java.util.Properties;

/**
 * ����Log4J�����ļ����࣬��װ��Log4J��������Ϣ<BR>
 * Log4J��4���������,���ϵ�����ERROR,WARN,INFO,DEBUG<BR>
 * ���������INFO������INFO�����ϼ��𶼻����
 * 
 * @author Recolar
 * @version 1.0.0 2007-1-6
 * @since JDK1.5.0
 */
public class Log4jProp {

	/**
	 * ����rootLogger����
	 */
	private String rootLogger;

	/**
	 * ������־�ļ����������·��
	 */
	private String logFilePath;

	/**
	 * ������־�ļ����������
	 */
	private int maxFileSize;

	/**
	 * ������־�ļ��ı����ļ���
	 */
	private int maxBackupIndex;

	/**
	 * �ڲ�����һ��Prepertiesʵ������ʵ�����ṩLog4J������Ϣ�Ļ���
	 */
	private Properties prop = new Properties();

	/**
	 * ������־�ļ����������·��,���Logger��û���ṩ������Ҫ�������logFile,���������Ч
	 * 
	 * @param logFilePath ����·��,��:"C:/a/b.log"
	 * @return Log4jPropʵ��
	 */
	public Log4jProp setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
		return this;
	}

	/**
	 * ������������־�ı�������
	 * 
	 * @param maxBackupIndex int ��������
	 * @return Log4jPropʵ��
	 */
	public Log4jProp setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
		return this;
	}

	/**
	 * ������������Log4jPropʵ��
	 * 
	 * @return Log4jProp
	 */
	public static Log4jProp getInstance() {
		return new Log4jProp();
	}

	/**
	 * ������־�ļ����������(��λ:KB)
	 * 
	 * @param maxFileSize
	 */
	public Log4jProp setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
		return this;
	}

	/**
	 * ����RootLogger����,����ʹ��LoggerParam�еĸ�����������������
	 * 
	 * @param rootLogger String
	 * @return
	 */
	public Log4jProp setRootLogger(String rootLogger) {
		this.rootLogger = rootLogger;
		return this;
	}

	/**
	 * ��ȡPrepertiesʵ��
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

	/** ��־����ı���ʽ */
	private static final String LOG_LAYOUT = "%d{yyyy-MM-dd HH:mm:ss,SSS} [%F:%L] %p:%m%n";

	/**
	 * �����������˵�����
	 * 
	 */
	private void loadBySetter() {
		if (this.rootLogger == null) {
			// prop.setProperty("log4j.rootLogger", "DEBUG, stdout,logfile");//
			// �������־
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
	 * Ĭ�ϼ���<br>
	 * Ĭ����Ϣ:1.���������־ 2.Ĭ�ϼ���ΪDebug��������
	 * 
	 */
	public Log4jProp loadDefault() {
		prop.setProperty("log4j.rootLogger", "DEBUG, stdout");// ���������־
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
