package com.rec.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 系统运行时异常
 * 
 * @author Recolar
 * @version 1.0.0 2007-2-17
 * @since JDK1.5
 * 
 */
public class RecRunTimeException extends RuntimeException {

	private final Log log = LogFactory.getLog(RecRunTimeException.class);

	private static final long serialVersionUID = 1371834197433616874L;

	public RecRunTimeException() {
		super();
		if (log.isErrorEnabled())
			log.error(getMessage(), this);
	}

	public RecRunTimeException(String message) {
		super(message);
		if (log.isErrorEnabled())
			log.error(message, this);
	}

	public RecRunTimeException(String message, Throwable e) {
		super(message, e);
		if (log.isErrorEnabled())
			log.error(message, e);
	}

	public RecRunTimeException(Throwable e) {
		super(e);
		if (log.isErrorEnabled())
			log.error(e.getMessage(), e);
	}
}
