package com.rec.frame.base;

import java.io.Serializable;

/**
 * ����ʵ�常��.
 * 
 * @author recolar
 * @version 1.0.0
 * @date 2013-9-19
 * @since JDK1.6.0
 * 
 */
public abstract class BaseEntity implements Serializable {

	/**
	 * ���������������дtoString()����
	 */
	public abstract String toString();

	/**
	 * ���������������дequals()����
	 */
	public abstract boolean equals(Object obj);

	/**
	 * ���������������дhashCode()����
	 */
	public abstract int hashCode();

}
