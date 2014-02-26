package com.rec.sys;

import com.rec.sys.impl.GlobalCache;

/**
 * ���𴴽��������Ĺ�����
 * 
 * @author Recolar
 * @see GlobalCache
 * @version 1.0.0 2006-12-29
 * @since JDK1.5.0
 * 
 */
public class CacheManager {

	private CacheManager() {
	}

	/**
	 * ��ȡһ���������
	 * 
	 * @param num ������
	 * 
	 * @return �������
	 */
	public static GlobalCache getGlobalCache() {
		return SysCache.getInstance();
	}
}
