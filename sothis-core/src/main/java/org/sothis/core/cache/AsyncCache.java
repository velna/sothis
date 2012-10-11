package org.sothis.core.cache;

import java.util.concurrent.Executor;

/**
 * 将所有put操作转化为异步实现。
 * 
 * @author velna
 * 
 */
public class AsyncCache extends FilterCache {

	private final Executor executor;

	public AsyncCache(Cache cache, Executor executor) {
		super(cache);
		this.executor = executor;
	}

	@Override
	public boolean put(final String key, final Object value, final long timeToLive) {
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				cache.put(key, value, timeToLive);
			}
		});
		return true;
	}

	@Override
	public boolean put(final String key, final CacheValue cacheValue) {
		this.executor.execute(new Runnable() {
			@Override
			public void run() {
				cache.put(key, cacheValue);
			}
		});
		return true;
	}

}
