package com.rec.web.fw.rc.repl;

/**
 * ��ִ��insert���ǰ��Ļص��ӿ�
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-2
 * @since JDK1.5.0
 * 
 */
public interface DBInsertListener {

	/**
	 * insert���ִ��֮ǰ
	 */
	public void beforeInsert();

	/**
	 * insert���ִ��֮��
	 * 
	 */
	public void afterInsert();
}
