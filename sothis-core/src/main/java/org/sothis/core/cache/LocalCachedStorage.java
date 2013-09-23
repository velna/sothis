package org.sothis.core.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;

/**
 * 包含一个本地缓存的缓存引擎代理。<br>
 * 本地缓存采用Google <a
 * href="http://code.google.com/p/guava-libraries">guava</a>项目的实现<br>
 * 所有的get会先从本地缓存中读取，如果本地缓存中没有再从实际缓存中取。<br>
 * 所有的put会先put到本地缓存，再put到实际缓存
 * 
 * @author velna
 * 
 */
public class LocalCachedStorage implements Storage {

	private final com.google.common.cache.Cache<String, CacheValue> localCache;
	private final Storage storage;

	/**
	 * 创建一个concurrencyLevel为50，soft value，存活60秒的本地缓存
	 * 
	 * @param cacheStore
	 */
	public LocalCachedStorage(Storage cacheStore) {
		this.storage = cacheStore;
		this.localCache = CacheBuilder.newBuilder().concurrencyLevel(50).softValues().expireAfterWrite(60, TimeUnit.SECONDS).build();
	}

	public LocalCachedStorage(Storage cacheStore, com.google.common.cache.Cache<String, CacheValue> localCache) {
		this.storage = cacheStore;
		this.localCache = localCache;
	}

	@Override
	public CacheValue get(String key) {
		CacheValue ret = localCache.getIfPresent(key);
		if (null == ret) {
			ret = storage.get(key);
			localCache.put(key, ret);
		}
		return ret;
	}

	@Override
	public Map<String, CacheValue> get(Collection<String> keyList) {
		Set<String> keySet = new HashSet<String>(keyList);
		Map<String, CacheValue> retMap = new HashMap<String, CacheValue>(keySet.size());
		Set<String> actualKeySet = new HashSet<String>(keySet.size());
		for (String k : keySet) {
			CacheValue value = (CacheValue) localCache.getIfPresent(k);
			if (null != value) {
				retMap.put(k, value);
			} else {
				actualKeySet.add(k);
			}
		}
		if (actualKeySet.size() > 0) {
			Map<String, CacheValue> ret = storage.get(actualKeySet);
			if (null != ret) {
				for (Map.Entry<String, CacheValue> entry : ret.entrySet()) {
					CacheValue value = entry.getValue();
					localCache.put(entry.getKey(), value);
					retMap.put(entry.getKey(), value);
				}
			}
		}
		return retMap;
	}

	@Override
	public boolean put(String key, CacheValue value) {
		localCache.put(key, value);
		return storage.put(key, value);
	}

	@Override
	public boolean remove(String key) {
		localCache.invalidate(key);
		return storage.remove(key);
	}

	@Override
	public long size() {
		return storage.size();
	}

}
