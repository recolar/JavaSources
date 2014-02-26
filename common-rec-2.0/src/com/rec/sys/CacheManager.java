package com.rec.sys;

import com.rec.sys.impl.GlobalCache;

/**
 * 负责创建缓存对象的工厂类
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
	 * 获取一个缓存对象
	 * 
	 * @param num 缓存标记
	 * 
	 * @return 缓存对象
	 */
	public static GlobalCache getGlobalCache() {
		return SysCache.getInstance();
	}
}
