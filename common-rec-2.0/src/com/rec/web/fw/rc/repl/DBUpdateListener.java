package com.rec.web.fw.rc.repl;

/**
 * 在执行update语句前后的回调接口
 * 
 * @author Recolar
 * @version 1.0.0 2007-6-2
 * @since JDK1.5.0
 * 
 */
public interface DBUpdateListener {

	/**
	 * update语句执行之前
	 */
	public void beforeUpdate();

	/**
	 * update语句执行之后
	 * 
	 */
	public void afterUpdate();
}
