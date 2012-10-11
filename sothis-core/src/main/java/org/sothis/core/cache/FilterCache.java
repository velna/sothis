package org.sothis.core.cache;

import java.util.Collection;
import java.util.Map;

import org.sothis.core.util.Closure;

public class FilterCache implements Cache {

	protected final Cache cache;

	public FilterCache(Cache cache) {
		this.cache = cache;
	}

	@Override
	public <V> V get(String key) {
		return cache.get(key);
	}

	@Override
	public <V> V get(String key, Closure<CacheValue, String> closure) {
		return cache.get(key, closure);
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keys) {
		return cache.get(extraKey, keys);
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keys, Closure<Map<String, CacheValue>, Collection<String>> closure) {
		return cache.get(extraKey, keys, closure);
	}

	@Override
	public boolean put(String key, Object value, long timeToLive) {
		return cache.put(key, value, timeToLive);
	}

	@Override
	public boolean put(String key, CacheValue cacheValue) {
		return cache.put(key, cacheValue);
	}

	@Override
	public boolean remove(String key) {
		return cache.remove(key);
	}

	@Override
	public boolean remove(String extraKey, String key) {
		return cache.remove(extraKey, key);
	}

	@Override
	public CacheStat stats() {
		return cache.stats();
	}

	@Override
	public Storage getStorage() {
		return cache.getStorage();
	}

}
