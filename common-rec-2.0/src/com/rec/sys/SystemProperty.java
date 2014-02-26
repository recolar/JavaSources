package com.rec.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统缓存<br>
 * 系统属性,单例模式构建，保证在系统运行中只存在一个实例达到属性共享的目的<br>
 * 
 * 
 * @author Recolar
 * @see ISystemProperty
 * @version 1.0.0 2006-12-29
 * @since JDK1.5.0
 * 
 */
public class SystemProperty implements ISystemProperty {

	private static SystemProperty sysProp;

	private Map propsMap;

	private SystemProperty() {
		if (propsMap == null)
			propsMap = new HashMap();
	}

	/**
	 * 获取SystemProperty的实例
	 * 
	 * @return SystemProperty的实例
	 */
	public static SystemProperty getInstance() {
		if (sysProp == null) {
			sysProp = new SystemProperty();
		}
		return sysProp;
	}

	/**
	 * 添加一对属性
	 * 
	 * @param name
	 * @param value
	 * @return 添加成功返回true否则返回false
	 */
	public void addProperty(Object name, Object value) {
		propsMap.put(name, value);
	}

	/**
	 * 删除一个属性
	 * 
	 * @param name
	 * @param value
	 * @return 删除成功返回true否则返回false
	 */
	public boolean removeProperty(Object name) {
		Object obj = propsMap.remove(name);
		return obj == null ? false : true;
	}

	/**
	 * 获取一共有多少个属性节点
	 * 
	 * @return 属性数目
	 */
	public int getPropertiesCount() {
		return propsMap.size();
	}

	/**
	 * 以Map的形式返回当前的属性键值对
	 * 
	 * @return Map
	 */
	public Map getPropertiesMap() {
		return this.propsMap;
	}

	/**
	 * 检查是否包含某个属性节点
	 * 
	 * @param name
	 * @return 如果包含则返回true否则返回false
	 */
	public boolean contains(Object name) {
		return propsMap.containsKey(name) ? true : false;
	}

	/**
	 * 返回某个属性值
	 * 
	 * @param 属性name
	 * @param 属性value
	 */
	public Object getProperty(Object name) {
		return propsMap.containsKey(name) ? propsMap.get(name) : null;
	}

	/**
	 * 检查是否为空
	 * 
	 * @return 如果为空则返回true否则返回false
	 */
	public boolean isEmpty() {
		return propsMap.isEmpty();
	}

}
