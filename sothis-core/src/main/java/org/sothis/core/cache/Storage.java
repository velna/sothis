package org.sothis.core.cache;

import java.util.Collection;
import java.util.Map;

/**
 * 缓存的存储引擎
 * 
 * @author velna
 * 
 */
public interface Storage {

	/**
	 * 取出键值为{@code key}的缓存值
	 * 
	 * @param key
	 * @return
	 */
	CacheValue get(String key);

	/**
	 * 根据{@code keys}得到缓存值
	 * 
	 * @param keys
	 * @return key为{@code keys}中的值，value为该key对应的缓存值
	 */
	Map<String, CacheValue> get(Collection<String> keys);

	/**
	 * 将{@code value}放到{@code key}对应的缓存中
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	boolean put(String key, CacheValue value);

	/**
	 * 从缓存中删除键为{@code key}的值
	 * 
	 * @param key
	 * @return
	 */
	boolean remove(String key);

	/**
	 * 得到缓存中的记录条数。
	 * 
	 * @return
	 */
	long size();
}
