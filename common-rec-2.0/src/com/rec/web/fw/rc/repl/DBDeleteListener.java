package com.rec.web.fw.rc.repl;

/**
 * ��ִ��delete���ǰ��Ļص��ӿ�
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-2
 * @since JDK1.5.0
 * 
 */
public interface DBDeleteListener {

	/**
	 * delete���ִ��֮ǰ
	 */
	public void beforedelete();

	/**
	 * delete���ִ��֮��
	 * 
	 */
	public void afterdelete();
}
