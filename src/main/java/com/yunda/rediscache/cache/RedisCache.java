package com.yunda.rediscache.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;

import com.yunda.rediscache.dao.RedisDao;


public class RedisCache implements Cache{
	private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private RedisDao redisDao;
	
	private String appName;
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
    /** --------------->> 支持spring自带的cache操作 <<--------------- */
	@Override
	public void evict(Object key) {
		if(key == null) return;
		DataType cacheType= redisTemplate.type(key.toString());
		if(!cacheType.equals(DataType.NONE))
			redisDao.delete(key.toString());
	}

	@Override
	public ValueWrapper get(Object key) {
		if(key == null) return null;
		String keyTemp = key.toString();
		DataType cacheType= redisTemplate.type(keyTemp);
		logger.info("key:"+key+", type:"+cacheType);
		if(cacheType.equals(DataType.LIST))
			return new SimpleValueWrapper(redisDao.getList(keyTemp));
		else if(cacheType.equals(DataType.HASH))
			return new SimpleValueWrapper(redisDao.getMap(keyTemp));
		else if(cacheType.equals(DataType.SET))
			return new SimpleValueWrapper(redisDao.getSet(keyTemp));
		else if(cacheType.equals(DataType.STRING))
			return new SimpleValueWrapper(redisDao.get(keyTemp));
		else return null;    //datatype中zset(暂时没实现)和none类型,返回null
	}

	@Override
	public String getName() {
		return this.appName;
	}

	@Override
	public Object getNativeCache() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void put(Object key, Object value) {
		logger.info("key:"+key+", value:"+value);
		String keyTemp = key.toString();
		if(value instanceof List)
			redisDao.setList(keyTemp, (List<Object>)value);
		else if(value instanceof Map)
			redisDao.putAll(keyTemp, (Map<Object, Object>)value);
		else if(value instanceof Set)
			redisDao.addSet(keyTemp, (Set<Object>)value);
		else
			redisDao.set(keyTemp, value);
	}

	@Override
	public void clear() {
		redisDao.clear();
	}
}
