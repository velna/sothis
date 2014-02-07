package org.sothis.core.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.sothis.core.util.Closure;

/**
 * 缓存一个基本实现
 * 
 * @author velna
 * 
 */
public class BasicCache implements Cache {

	private final CacheStat stats;
	private final Storage storage;

	public BasicCache(String cacheStatName, Storage storage) {
		stats = new CacheStat(cacheStatName);
		this.storage = storage;
	}

	@Override
	public <V> V get(String key) {
		CacheValue cacheValue = this.get0(key);
		if (null == cacheValue) {
			return null;
		} else {
			return cacheValue.getValue();
		}
	}

	@Override
	public <V> V get(String key, Closure<CacheValue, String> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("null closure");
		}
		CacheValue ret = this.get0(key);
		if (null == ret) {
			ret = closure.execute(key);
			if (null != ret) {
				this.put0(buildCacheKey(key), ret);
				return ret.getValue();
			} else {
				return null;
			}
		} else {
			return ret.getValue();
		}
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keyList) {
		List<String> keys = new ArrayList<String>(keyList.size());
		Map<String, String> keyMap = new HashMap<String, String>(keyList.size());
		for (String key : keyList) {
			String cacheKey = buildCacheKey(extraKey, key);
			keys.add(cacheKey);
			keyMap.put(cacheKey, key);
		}
		Map<String, CacheValue> valueMap = storage.get(keys);
		Map<String, V> ret = new HashMap<String, V>(valueMap.size());
		for (Map.Entry<String, CacheValue> entry : valueMap.entrySet()) {
			V value = entry.getValue().getValue();
			ret.put(keyMap.get(entry.getKey()), value);
		}
		stats.requestGet(valueMap.size(), keyList.size() - valueMap.size());
		return ret;
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keyList, Closure<Map<String, CacheValue>, Collection<String>> closure) {
		if (null == closure) {
			throw new IllegalArgumentException("null closure");
		}
		if (CollectionUtils.isEmpty(keyList)) {
			return Collections.emptyMap();
		}
		Map<String, V> ret = new HashMap<String, V>(keyList.size());

		List<String> keys = new ArrayList<String>(keyList.size());
		Map<String, String> keyMap = new HashMap<String, String>(keyList.size());
		for (String key : keyList) {
			String cacheKey = buildCacheKey(extraKey, key);
			keys.add(cacheKey);
			keyMap.put(key, cacheKey);
		}
		Map<String, CacheValue> valueMap = storage.get(keys);
		List<String> nullValueKeys = new ArrayList<String>();
		for (String key : keyList) {
			String cacheKey = keyMap.get(key);
			CacheValue cacheValue = valueMap.get(cacheKey);
			if (null == cacheValue) {
				nullValueKeys.add(key);
			} else {
				V value = cacheValue.getValue();
				ret.put(key, value);
			}
		}

		// 缓存中没有的数据，调用闭包获取
		if (nullValueKeys.size() > 0) {
			Map<String, CacheValue> exeRet = closure.execute(nullValueKeys);
			if (null != exeRet) {
				for (Map.Entry<String, CacheValue> entry : exeRet.entrySet()) {
					CacheValue cacheValue = entry.getValue();
					if (null != cacheValue) {
						String cacheKey = buildCacheKey(extraKey, entry.getKey());
						put0(cacheKey, cacheValue);
						V value = cacheValue.getValue();
						ret.put(entry.getKey(), value);
					}
				}
			}
		}
		stats.requestGet(valueMap.size(), keyList.size() - valueMap.size());
		return ret;
	}

	@Override
	public boolean put(String key, Object value, long timeToLive) {
		return this.put0(key, new CacheValue(value, timeToLive));
	}

	@Override
	public boolean put(String key, CacheValue cacheValue) {
		return this.put0(key, cacheValue);
	}

	@Override
	public boolean remove(String key) {
		return this.storage.remove(key);
	}

	@Override
	public boolean remove(String extraKey, String key) {
		return this.storage.remove(this.buildCacheKey(extraKey, key));
	}

	private CacheValue get0(Object key) {
		String cacheKey = buildCacheKey(key);
		CacheValue ret = storage.get(cacheKey);
		if (null == ret) {
			stats.requestGet(0, 1);
		} else {
			stats.requestGet(1, 0);
		}
		return ret;
	}

	private boolean put0(String key, CacheValue value) {
		stats.requestPut();
		return storage.put(key, value);
	}

	protected String buildCacheKey(Object... keys) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < keys.length; i++) {
			ret.append(keys[i]);
			if (i < keys.length - 1) {
				ret.append('_');
			}
		}
		return ret.toString();
	}

	@Override
	public CacheStat stats() {
		return stats;
	}

	@Override
	public Storage getStorage() {
		return storage;
	}

	@Override
	public long size() {
		return storage.size();
	}

}
