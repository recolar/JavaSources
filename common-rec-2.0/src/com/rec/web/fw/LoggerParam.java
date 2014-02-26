package com.rec.web.fw;

/**
 * LoggerParam是Log4jProp中设置Logger的常量<br>
 * 用于在Log4jProp实例中setRootLogger方法中提供参数<br>
 * <br>
 * Log4J日志输出可以设置的级别权限从低到高是:trace,debug,info,warn,error,fatal,<br>
 * 如果设置了info级别输出，则info以下级别的日志项目将不会输出
 * 
 * @author recolar
 * 
 * @see Log4jProp
 * @version 1.0.0 2007-1-6
 * @since JDK1.4.2.6
 */
public interface LoggerParam {

	/* 下面定义LOGGER组件 */

	/**
	 * 打开全部输出级别
	 */
	public static final String LOGGER_ALL = "all, stdout,logfile";

	/**
	 * 关闭所有输出级别
	 */
	public static final String LOGGER_OFF = "off";

	/**
	 * 设置trace输出级别,输出到控制台，输出到日志
	 */
	public static final String TRACE_STDOUT_LOGFILE = "trace, stdout,logfile";

	/**
	 * 设置trace输出级别,只输出到日志
	 */
	public static final String TRACE_LOGFILE = "trace, logfile";

	/**
	 * 设置trace输出级别，只输出到控制台
	 */
	public static final String TRACE_STDOUT = "trace, stdout";

	/**
	 * 设置debug输出级别，输出到控制台，输出到日志
	 */
	public static final String DEBUG_STDOUT_LOGFILE = "DEBUG, stdout,logfile";

	/**
	 * 设置debug输出级别，只输出到日志
	 */
	public static final String DEBUG_LOGFILE = "DEBUG, logfile";

	/**
	 * 设置debug输出级别，只输出到控制台
	 */
	public static final String DEBUG_STDOUT = "DEBUG, stdout";

	/**
	 * 设置info输出级别，输出到控制台，输出到日志
	 */
	public static final String INFO_STDOUT_LOGFILE = "INFO, stdout,logfile";

	/**
	 * 设置info输出级别，只输出到日志
	 */
	public static final String INFO_LOGFILE = "INFO, logfile";

	/**
	 * 设置info输出级别，只输出到控制台
	 */
	public static final String INFO_STDOUT = "INFO, stdout";

	/**
	 * 设置warn输出级别，输出到控制台，输出到日志
	 */
	public static final String WARN_STDOUT_LOGFILE = "WARN, stdout,logfile";

	/**
	 * 设置warn输出级别，只输出到日志
	 */
	public static final String WARN_LOGFILE = "WARN, logfile";

	/**
	 * 设置warn输出级别，只输出到控制台
	 */
	public static final String WARN_STDOUT = "WARN, stdout";

	/**
	 * 设置error输出级别，输出到控制台，输出到日志
	 */
	public static final String ERROR_STDOUT_LOGFILE = "ERROR, stdout,logfile";

	/**
	 * 设置error输出级别，只输出到日志
	 */
	public static final String ERROR_LOGFILE = "ERROR, logfile";

	/**
	 * 设置error输出级别，只输出到控制台
	 */
	public static final String ERROR_STDOUT = "ERROR, stdout";

	/**
	 * 设置fatal输出级别，输出到控制台，输出到日志
	 */
	public static final String FATAL_STDOUT_LOGFILE = "FATAL, stdout,logfile";

	/**
	 * 设置fatal输出级别，只输出到日志
	 */
	public static final String FATAL_LOGFILE = "FATAL, logfile";

	/**
	 * 设置fatal输出级别，只输出到控制台
	 */
	public static final String FATAL_STDOUT = "FATAL, stdout";
}
