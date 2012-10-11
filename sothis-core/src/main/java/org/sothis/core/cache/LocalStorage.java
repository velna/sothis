package org.sothis.core.cache;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

/**
 * 基本Google <a
 * href="http://code.google.com/p/guava-libraries">guava</a>项目的缓存引擎实现。
 * 
 * @author velna
 * 
 */
public class LocalStorage implements Storage {

	private final com.google.common.cache.Cache<String, CacheValue> cache;

	public LocalStorage(int concurrencyLevel, long duration) {
		cache = CacheBuilder.newBuilder().concurrencyLevel(concurrencyLevel).softValues().expireAfterWrite(duration, TimeUnit.SECONDS).build();
	}

	public LocalStorage(com.google.common.cache.Cache<String, CacheValue> cache) {
		this.cache = cache;
	}

	@Override
	public CacheValue get(String key) {
		return cache.getIfPresent(key);
	}

	@Override
	public Map<String, CacheValue> get(Collection<String> keyList) {
		return cache.getAllPresent(keyList);
	}

	@Override
	public boolean put(String key, CacheValue value) {
		cache.put(key, value);
		return true;
	}

	@Override
	public boolean remove(String key) {
		cache.invalidate(key);
		return true;
	}

}
