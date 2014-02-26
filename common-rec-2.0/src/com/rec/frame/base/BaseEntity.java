package com.rec.frame.base;

import java.io.Serializable;

/**
 * 抽象实体父类.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-19
 * @since JDK1.6.0
 * 
 */
public abstract class BaseEntity implements Serializable {

	/**
	 * 所有派生类必须重写toString()方法
	 */
	public abstract String toString();

	/**
	 * 所有派生类必须重写equals()方法
	 */
	public abstract boolean equals(Object obj);

	/**
	 * 所有派生类必须重写hashCode()方法
	 */
	public abstract int hashCode();

}
