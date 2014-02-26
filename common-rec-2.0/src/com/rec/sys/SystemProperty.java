package com.rec.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * ϵͳ����<br>
 * ϵͳ����,����ģʽ��������֤��ϵͳ������ֻ����һ��ʵ���ﵽ���Թ����Ŀ��<br>
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
	 * ��ȡSystemProperty��ʵ��
	 * 
	 * @return SystemProperty��ʵ��
	 */
	public static SystemProperty getInstance() {
		if (sysProp == null) {
			sysProp = new SystemProperty();
		}
		return sysProp;
	}

	/**
	 * ���һ������
	 * 
	 * @param name
	 * @param value
	 * @return ��ӳɹ�����true���򷵻�false
	 */
	public void addProperty(Object name, Object value) {
		propsMap.put(name, value);
	}

	/**
	 * ɾ��һ������
	 * 
	 * @param name
	 * @param value
	 * @return ɾ���ɹ�����true���򷵻�false
	 */
	public boolean removeProperty(Object name) {
		Object obj = propsMap.remove(name);
		return obj == null ? false : true;
	}

	/**
	 * ��ȡһ���ж��ٸ����Խڵ�
	 * 
	 * @return ������Ŀ
	 */
	public int getPropertiesCount() {
		return propsMap.size();
	}

	/**
	 * ��Map����ʽ���ص�ǰ�����Լ�ֵ��
	 * 
	 * @return Map
	 */
	public Map getPropertiesMap() {
		return this.propsMap;
	}

	/**
	 * ����Ƿ����ĳ�����Խڵ�
	 * 
	 * @param name
	 * @return ��������򷵻�true���򷵻�false
	 */
	public boolean contains(Object name) {
		return propsMap.containsKey(name) ? true : false;
	}

	/**
	 * ����ĳ������ֵ
	 * 
	 * @param ����name
	 * @param ����value
	 */
	public Object getProperty(Object name) {
		return propsMap.containsKey(name) ? propsMap.get(name) : null;
	}

	/**
	 * ����Ƿ�Ϊ��
	 * 
	 * @return ���Ϊ���򷵻�true���򷵻�false
	 */
	public boolean isEmpty() {
		return propsMap.isEmpty();
	}

}
