package com.jca.datatool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存工具
 * @author Administrator
 *
 */
public class CacheUtil {
	public static final Map<String,Object> cache = new ConcurrentHashMap<String, Object>();
	
	public static void put(String key,Object object) {
		if(cache.containsKey(key))
			return;
		cache.put(key, object);
	}
	
	public static Object get(Object key) {
		if(cache.containsKey(key))
			return cache.get(key);
		return null;
	}
}
