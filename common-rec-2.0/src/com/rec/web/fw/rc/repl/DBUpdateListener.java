package com.rec.web.fw.rc.repl;

/**
 * ��ִ��update���ǰ��Ļص��ӿ�
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-2
 * @since JDK1.5.0
 * 
 */
public interface DBUpdateListener {

	/**
	 * update���ִ��֮ǰ
	 */
	public void beforeUpdate();

	/**
	 * update���ִ��֮��
	 * 
	 */
	public void afterUpdate();
}
