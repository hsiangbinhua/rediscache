package com.yunda.rediscache.cache;

import java.util.Collection;
import java.util.Collections;

import javax.annotation.Resource;


import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.util.Assert;

public class RedisCacheManager extends AbstractCacheManager{
	@Resource
	private RedisCache redisCache;
	
	@Override
    public Cache getCache(String name) {
		Assert.notNull(redisCache);
		System.out.println("cacheName:"+name);
        Cache cache = super.getCache(name);
        if(cache != null)	return cache;
        Cache cacheNew = redisCache;
        super.addCache(cacheNew);
        return cacheNew;
    }
	
	@Override
	protected Collection<? extends Cache> loadCaches() {
		return Collections.emptyList();
	}

}
