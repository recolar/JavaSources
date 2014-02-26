package com.rec.sys;

import java.util.HashMap;
import java.util.Map;

import com.rec.sys.impl.GlobalCache;

/**
 * ϵͳ����<br>
 * ϵͳ����,����ģʽ��������֤��ϵͳ������ֻ����һ��ʵ���ﵽ���Թ����Ŀ��<br>
 * ��GlobalManager�������ò�����GlobalCache
 * 
 * 
 * @author Recolar
 * @see GlobalCache
 * @version 1.0.0 2006-12-29
 * @since JDK1.5.0
 * 
 */
final class SysCache implements GlobalCache {

	private static SysCache sysProp;

	private Map propsMap;

	private SysCache() {
		if (propsMap == null)
			propsMap = new HashMap();
	}

	/**
	 * ��ȡSystemProperty��ʵ��
	 * 
	 * @return SystemProperty��ʵ��
	 */
	static SysCache getInstance() {
		if (sysProp == null) {
			sysProp = new SysCache();
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
	 * ɾ����������
	 * 
	 */
	public void clearAllProperties() {
		propsMap.clear();
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
