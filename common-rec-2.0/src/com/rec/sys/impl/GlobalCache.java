package com.rec.sys.impl;

import java.util.Map;

/**
 * 系统属性类的接口
 * 
 * @author Recolar
 * @see SysCache
 * @see Map
 * @version 1.0.0 2006-12-29
 * @since JDK1.5.0
 * 
 */
public interface GlobalCache {

	void addProperty(Object name, Object value);

	boolean removeProperty(Object name);

	int getPropertiesCount();

	Map getPropertiesMap();

	boolean contains(Object name);

	Object getProperty(Object name);

	boolean isEmpty();

	void clearAllProperties();
}
