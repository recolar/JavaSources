package com.rec.web.fw.rc.repl;

/**
 * 在执行delete语句前后的回调接口
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-2
 * @since JDK1.5.0
 * 
 */
public interface DBDeleteListener {

	/**
	 * delete语句执行之前
	 */
	public void beforedelete();

	/**
	 * delete语句执行之后
	 * 
	 */
	public void afterdelete();
}
