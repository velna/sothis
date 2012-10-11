package org.sothis.core.cache;

import com.google.common.base.Function;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class CacheFactory {
	private final Storage storage;
	private final com.google.common.cache.Cache<Class<?>, Cache> caches;

	public CacheFactory(Storage storage) {
		this.storage = storage;
		caches = CacheBuilder.newBuilder().concurrencyLevel(50).build(CacheLoader.from(new Function<Class<?>, Cache>() {

			@Override
			public Cache apply(Class<?> key) {
				return new RegionalCache(key.getName(), CacheFactory.this.storage);
			}

		}));
	}

	public Cache getCache(Class<?> region) {
		return caches.getIfPresent(region);
	}
}
