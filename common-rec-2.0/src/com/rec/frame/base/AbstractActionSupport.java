package com.rec.frame.base;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action的抽象父类，所有Action必须派生至此类.
 * 
 * @author Administrator
 * @version 1.0.0
 * @date 2013-9-19
 * @since JDK1.6.0
 * 
 */
public abstract class AbstractActionSupport extends ActionSupport implements Serializable {

	/**
	 * Constructor.
	 */
	public AbstractActionSupport() {
	}

	/** 定义Log对象. */
	protected static final Log LOG = LogFactory.getLog(AbstractActionSupport.class);

	/** 是否为Debug控制级别. */
	protected static final boolean ISDEBUG = LOG.isDebugEnabled();

	/** 是否为Info控制级别. */
	protected static final boolean ISINFO = LOG.isInfoEnabled();

	/** 是否为Warn控制级别. */
	protected static final boolean ISWARN = LOG.isWarnEnabled();

	/** 是否为ISERROR控制级别. */
	protected static final boolean ISERROR = LOG.isErrorEnabled();

	private static final String CONTENT_TYPE_UTF8 = "text/html;charset=UTF-8";

	/**
	 * 将字符串信息响应到客户端.用于响应视图中以Ajax方式提交的请求.
	 * 
	 * @param message 需要输出至客户端的字符串
	 */
	protected void printRemoteResponse(String message) {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType(CONTENT_TYPE_UTF8);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			if (LOG.isDebugEnabled()) {
				LOG.debug(message);
			}
			out.print(message);
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(e.getMessage(), e);
			}
			throw new RuntimeException(e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
}
