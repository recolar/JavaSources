package com.rec.frame.base;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Action�ĳ����࣬����Action��������������.
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

	/** ����Log����. */
	protected static final Log LOG = LogFactory.getLog(AbstractActionSupport.class);

	/** �Ƿ�ΪDebug���Ƽ���. */
	protected static final boolean ISDEBUG = LOG.isDebugEnabled();

	/** �Ƿ�ΪInfo���Ƽ���. */
	protected static final boolean ISINFO = LOG.isInfoEnabled();

	/** �Ƿ�ΪWarn���Ƽ���. */
	protected static final boolean ISWARN = LOG.isWarnEnabled();

	/** �Ƿ�ΪISERROR���Ƽ���. */
	protected static final boolean ISERROR = LOG.isErrorEnabled();

	private static final String CONTENT_TYPE_UTF8 = "text/html;charset=UTF-8";

	/**
	 * ���ַ�����Ϣ��Ӧ���ͻ���.������Ӧ��ͼ����Ajax��ʽ�ύ������.
	 * 
	 * @param message ��Ҫ������ͻ��˵��ַ���
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
