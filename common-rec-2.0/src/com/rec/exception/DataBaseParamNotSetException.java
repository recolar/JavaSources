package com.rec.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * DBUtil类中的没有设置数据库连接参数所抛出的异常处理<br>
 * 
 * @author Recolar
 * @version 1.0.0 2006-12-02
 * @since JDK1.5.0
 */
public class DataBaseParamNotSetException extends Exception {

	private static final Log log = LogFactory.getLog(DataBaseParamNotSetException.class);

	private static final long serialVersionUID = 1L;

	public DataBaseParamNotSetException() {
		super();
		if (log.isErrorEnabled())
			log.error(getMessage(), this);
	}

	public DataBaseParamNotSetException(String message) {
		super(message);
		if (log.isErrorEnabled())
			log.error(message, this);
	}

	public DataBaseParamNotSetException(String message, Throwable e) {
		super(message, e);
		if (log.isErrorEnabled())
			log.error(message, e);
	}

	public DataBaseParamNotSetException(Throwable e) {
		super(e);
		if (log.isErrorEnabled())
			log.error(e.getMessage(), e);
	}
}
