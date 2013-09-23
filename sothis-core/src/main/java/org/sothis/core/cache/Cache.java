package org.sothis.core.cache;

import java.util.Collection;
import java.util.Map;

import org.sothis.core.util.Closure;

/**
 * 缓存接口。<br>
 * null值也会被放入到缓存中去。
 * 
 * @author velna
 * 
 */
public interface Cache {
	/**
	 * 从缓存中取出键为{@code key}的值
	 * 
	 * @param key
	 *            缓存的键值
	 * @return {@code key}对应的缓存值
	 */
	<V> V get(String key);

	/**
	 * 从缓存中取出键为{@code key}的值，如果缓存中找不到，则调用{@code closure}来生成缓存，并将该值更新到缓存中去。
	 * 
	 * @param key
	 *            缓存的键值
	 * @param closure
	 *            生成{@code key}对应的缓存值的闭包
	 * @return {@code key}对应的缓存值
	 */
	<V> V get(String key, Closure<CacheValue, String> closure);

	/**
	 * 根据一组键值，得到这些键所对应的缓存。{@code extraKey}和{@code keys}中的每一个值进行拼接后做为实际用来取缓存的键值。
	 * 
	 * @param extraKey
	 *            额外的键值
	 * @param keys
	 *            需要查找的key集合
	 * @return 返回一个Map，key为{@code keys}中的一个值，value为该key对应的缓存值
	 */
	<V> Map<String, V> get(String extraKey, Collection<String> keys);

	/**
	 * 根据一组键值，得到这些键所对应的缓存。{@code extraKey}和{@code keys}中的每一个值进行拼接后做为实际用来取缓存的键值。<br>
	 * 如果缓存中找不到{@code keys}中的一个或多个缓存，这些key会通过调用{@code closure}来生成。
	 * 
	 * @param extraKey
	 *            额外的键值
	 * @param keys
	 *            需要查找的key集合
	 * @param closure
	 * @return 返回一个Map，key为{@code keys}中的一个值，value为该key对应的缓存值
	 */
	<V> Map<String, V> get(String extraKey, Collection<String> keys, Closure<Map<String, CacheValue>, Collection<String>> closure);

	/**
	 * 将{@code value}放到{@code key}对应的缓存中。
	 * 
	 * @param key
	 *            缓存键
	 * @param value
	 *            缓存值
	 * @param timeToLive
	 *            缓存的存活时间，以秒为单位
	 * @return 成功返回true，否则返回false
	 */
	boolean put(String key, Object value, long timeToLive);

	/**
	 * 将{@code cacheValue}放到{@code key}对应的缓存中。
	 * 
	 * @param key
	 *            缓存键
	 * @param cacheValue
	 *            缓存值
	 * @return 成功返回true，否则返回false
	 */
	boolean put(String key, CacheValue cacheValue);

	/**
	 * 从缓存中删除键值为{@code key}的缓存。
	 * 
	 * @param key
	 *            要删除的缓存键值
	 * @return 成功返回true，否则返回false
	 */
	boolean remove(String key);

	/**
	 * 从缓存中删除键值为{@code extraKey}和{@code key}的缓存。
	 * 
	 * @param extraKey
	 * @param key
	 * @return 成功返回true，否则返回false
	 */
	boolean remove(String extraKey, String key);

	/**
	 * 得到缓存中的记录数。
	 * 
	 * @return
	 */
	long size();

	/**
	 * 得到缓存的统计情况
	 * 
	 * @return
	 */
	CacheStat stats();

	/**
	 * 得到缓存的存储引擎
	 * 
	 * @param storage
	 */
	Storage getStorage();
}
