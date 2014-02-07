package org.sothis.core.cache;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.sothis.core.util.Closure;
import org.sothis.core.util.ExecuteCounter;
import org.sothis.core.util.LoggerFactory;

/**
 * 记录了缓存性能日志
 * 
 * @author velna
 * 
 */
public class PerformanceLogCache extends FilterCache {
	private static final Logger PERFORMANCE_LOGGER = LoggerFactory.getPerformanceLogger(PerformanceLogCache.class);
	/**
	 * 缓存调用日志的ThreadLocal key。
	 * 
	 * @see {@link ExecuteCounter}
	 */
	public final static String EXECUTE_COUNTER_KEY = PerformanceLogCache.class.getSimpleName();
	private static final int PERFORMENCE_FACTOR = 100;

	public PerformanceLogCache(Cache cache) {
		super(cache);
	}

	@Override
	public <V> V get(String key) {
		return get0(key, null);
	}

	@Override
	public <V> V get(String key, Closure<CacheValue, String> closure) {
		return get0(key, closure);
	}

	private <V> V get0(String key, Closure<CacheValue, String> closure) {
		long start = System.currentTimeMillis();
		V ret = null;
		try {
			if (null == closure) {
				ret = cache.get(key);
			} else {
				ret = cache.get(key, closure);
			}
			return ret;
		} finally {
			long time = System.currentTimeMillis() - start;
			increaseExecuteCounter("get", time);
			if (time > PERFORMENCE_FACTOR) {
				if (ret == null) {
					logPerformence("get[miss]", time, key);
				} else {
					logPerformence("get[hit]", time, key);
				}
			}
		}
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keys) {
		return get0(extraKey, keys, null);
	}

	@Override
	public <V> Map<String, V> get(String extraKey, Collection<String> keys, Closure<Map<String, CacheValue>, Collection<String>> closure) {
		return get0(extraKey, keys, closure);
	}

	private <V> Map<String, V> get0(String extraKey, Collection<String> keys, Closure<Map<String, CacheValue>, Collection<String>> closure) {
		long start = System.currentTimeMillis();
		Map<String, V> ret = null;
		try {
			if (null == closure) {
				ret = cache.get(extraKey, keys);
			} else {
				ret = cache.get(extraKey, keys, closure);
			}
			return ret;
		} finally {
			long time = System.currentTimeMillis() - start;
			increaseExecuteCounter("get", time);
			if (time > PERFORMENCE_FACTOR) {
				logPerformence("get[multi:" + ret.size() + "/" + keys.size() + "]", time, prettyString(ret.keySet()));
			}
		}
	}

	@Override
	public boolean put(String key, Object value, long timeToLive) {
		return this.put0(key, new CacheValue(value, timeToLive));
	}

	@Override
	public boolean put(String key, CacheValue cacheValue) {
		return this.put0(key, cacheValue);
	}

	private boolean put0(String key, CacheValue cacheValue) {
		long start = System.currentTimeMillis();
		boolean ret = false;
		try {
			ret = cache.put(key, cacheValue);
			return ret;
		} finally {
			long time = System.currentTimeMillis() - start;
			increaseExecuteCounter("put", time);
			if (time > PERFORMENCE_FACTOR) {
				if (ret) {
					logPerformence("set[success]", time, key);
				} else {
					logPerformence("set[fail]", time, key);
				}
			}
		}
	}

	@Override
	public boolean remove(String key) {
		long start = System.currentTimeMillis();
		boolean ret = false;
		try {
			ret = cache.remove(key);
			return ret;
		} finally {
			long time = System.currentTimeMillis() - start;
			increaseExecuteCounter("remove", time);
			if (time > PERFORMENCE_FACTOR) {
				if (ret) {
					logPerformence("remove[success]", time, key);
				} else {
					logPerformence("remove[fail]", time, key);
				}
			}
		}
	}

	@Override
	public boolean remove(String extraKey, String key) {
		long start = System.currentTimeMillis();
		boolean ret = false;
		try {
			ret = cache.remove(extraKey, key);
			return ret;
		} finally {
			long time = System.currentTimeMillis() - start;
			increaseExecuteCounter("remove", time);
			if (time > PERFORMENCE_FACTOR) {
				if (ret) {
					logPerformence("remove[success]", time, key);
				} else {
					logPerformence("remove[fail]", time, key);
				}
			}
		}
	}

	protected void logPerformence(String action, long time, String key) {
		StringBuilder s = new StringBuilder();
		s.append(action);
		s.append(":\ttime=").append(time);
		s.append(",\tkey=").append(key);
		if (time < 500) {
			PERFORMANCE_LOGGER.warn(s.toString());
		} else {
			PERFORMANCE_LOGGER.error(s.toString());
		}
	}

	protected final void increaseExecuteCounter(String operation, long time) {
		ExecuteCounter.getThreadLocalInstance(EXECUTE_COUNTER_KEY).increase(this.getClass().getSimpleName(), operation, time);
	}

	private String prettyString(Collection<String> list) {
		StringBuilder ret = new StringBuilder();
		int i = 0;
		for (String s : list) {
			i++;
			if (i > 3) {
				ret.append("...").append(list.size() - 3).append(" more");
				break;
			} else {
				ret.append(s).append(',');
			}
		}
		return ret.toString();
	}

}
