package com.yunda.rediscache.cache;

import java.util.Collection;
import java.util.Collections;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

public class RedisCacheManager extends AbstractCacheManager {

	@Override
	public Cache getCache(String name) {
		System.out.println("cacheName:" + name);
		Cache cache = super.getCache(name);
		if (cache != null)
			return cache;
		Cache cacheNew = new RedisCache(name);
		super.addCache(cacheNew);
		return cacheNew;
	}

	@Override
	protected Collection<? extends Cache> loadCaches() {
		return Collections.emptyList();
	}

}