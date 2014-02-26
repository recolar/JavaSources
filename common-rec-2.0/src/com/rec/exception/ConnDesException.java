package com.rec.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SystemConfig.xml文件中参数配置不正确或者SystemConfig.xml文件不存在时抛出的异常
 * 
 * @author Recolar
 * @version 1.0.0 2006-12-09
 * @since JDK1.5.0
 */
public class ConnDesException extends RuntimeException {

	private final Log log = LogFactory.getLog(ConnDesException.class);

	private static final long serialVersionUID = 1094699568172190619L;

	public ConnDesException() {
		super();
		if (log.isErrorEnabled())
			log.error(getMessage(), this);
	}

	public ConnDesException(String message) {
		super(message);
		if (log.isErrorEnabled())
			log.error(message, this);
	}

	public ConnDesException(String message, Throwable e) {
		super(message, e);
		if (log.isErrorEnabled())
			log.error(message, e);
	}

	public ConnDesException(Throwable e) {
		super(e);
		if (log.isErrorEnabled())
			log.error(e.getMessage(), e);
	}
}
