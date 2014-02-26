package com.rec.web.fw;

/**
 * LoggerParam��Log4jProp������Logger�ĳ���<br>
 * ������Log4jPropʵ����setRootLogger�������ṩ����<br>
 * <br>
 * Log4J��־����������õļ���Ȩ�޴ӵ͵�����:trace,debug,info,warn,error,fatal,<br>
 * ���������info�����������info���¼������־��Ŀ���������
 * 
 * @author recolar
 * 
 * @see Log4jProp
 * @version 1.0.0 2007-1-6
 * @since JDK1.4.2.6
 */
public interface LoggerParam {

	/* ���涨��LOGGER��� */

	/**
	 * ��ȫ���������
	 */
	public static final String LOGGER_ALL = "all, stdout,logfile";

	/**
	 * �ر������������
	 */
	public static final String LOGGER_OFF = "off";

	/**
	 * ����trace�������,���������̨���������־
	 */
	public static final String TRACE_STDOUT_LOGFILE = "trace, stdout,logfile";

	/**
	 * ����trace�������,ֻ�������־
	 */
	public static final String TRACE_LOGFILE = "trace, logfile";

	/**
	 * ����trace�������ֻ���������̨
	 */
	public static final String TRACE_STDOUT = "trace, stdout";

	/**
	 * ����debug����������������̨���������־
	 */
	public static final String DEBUG_STDOUT_LOGFILE = "DEBUG, stdout,logfile";

	/**
	 * ����debug�������ֻ�������־
	 */
	public static final String DEBUG_LOGFILE = "DEBUG, logfile";

	/**
	 * ����debug�������ֻ���������̨
	 */
	public static final String DEBUG_STDOUT = "DEBUG, stdout";

	/**
	 * ����info����������������̨���������־
	 */
	public static final String INFO_STDOUT_LOGFILE = "INFO, stdout,logfile";

	/**
	 * ����info�������ֻ�������־
	 */
	public static final String INFO_LOGFILE = "INFO, logfile";

	/**
	 * ����info�������ֻ���������̨
	 */
	public static final String INFO_STDOUT = "INFO, stdout";

	/**
	 * ����warn����������������̨���������־
	 */
	public static final String WARN_STDOUT_LOGFILE = "WARN, stdout,logfile";

	/**
	 * ����warn�������ֻ�������־
	 */
	public static final String WARN_LOGFILE = "WARN, logfile";

	/**
	 * ����warn�������ֻ���������̨
	 */
	public static final String WARN_STDOUT = "WARN, stdout";

	/**
	 * ����error����������������̨���������־
	 */
	public static final String ERROR_STDOUT_LOGFILE = "ERROR, stdout,logfile";

	/**
	 * ����error�������ֻ�������־
	 */
	public static final String ERROR_LOGFILE = "ERROR, logfile";

	/**
	 * ����error�������ֻ���������̨
	 */
	public static final String ERROR_STDOUT = "ERROR, stdout";

	/**
	 * ����fatal����������������̨���������־
	 */
	public static final String FATAL_STDOUT_LOGFILE = "FATAL, stdout,logfile";

	/**
	 * ����fatal�������ֻ�������־
	 */
	public static final String FATAL_LOGFILE = "FATAL, logfile";

	/**
	 * ����fatal�������ֻ���������̨
	 */
	public static final String FATAL_STDOUT = "FATAL, stdout";
}
