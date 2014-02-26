package com.rec.sys;

import java.util.Map;

/**
 * 系统属性类的接口
 * 
 * @author Recolar
 * @see SystemProperty
 * @see Map
 * @version 1.0.0 2006-12-29
 * @since JDK1.5.0
 * 
 */
public interface ISystemProperty {

	public void addProperty(Object name, Object value);

	public boolean removeProperty(Object name);

	public int getPropertiesCount();

	public Map getPropertiesMap();

	public boolean contains(Object name);

	public Object getProperty(Object name);
	
	public boolean isEmpty();
}
