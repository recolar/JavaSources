package com.rec.web.fw.rc.repl;

/**
 * 数据库成功初始化时的回调接口
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-1
 * @since JDK1.5.0
 * 
 */
public interface DBInitialListener {

	/**
	 * 数据库初始化时的调用方法
	 * 
	 */
	public void dataBaseInitial();
}
