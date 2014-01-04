package com.yunda.rediscache.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;


public class RedisCacheImpl implements IRedisCache{
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Resource(name="redisTemplate")
	private ValueOperations<String, Object> valueOps;
	@Resource(name="redisTemplate")
	private ListOperations<String, Object> listOps;
	@Resource(name="redisTemplate")
	private SetOperations<String, Object> setOps;
	@Resource(name="redisTemplate")
	private HashOperations<String, Object, Object> hashOps;
	
	private String appName;
	public RedisCacheImpl(String appName){
		this.appName = appName;
	}
	
	/** -------------------->> pojo操作 <<-------------------- */
	@Override
	public boolean set(String key, Object value) {
		if(key == null || value == null)
			return false;
		valueOps.set(getUniqueKey(key), value);
		return true;
	}
	
	@Override
	public Object get(String key) {
		return valueOps.get(getUniqueKey(key));
	}
	
	/** --------------->> list操作 <<--------------- */
	@Override
	public boolean setList(String key, List<Object> list) {
		String uniqueKey = getUniqueKey(key);
		for (Object value: list)
			listOps.rightPush(uniqueKey, value);
		return true;
	}
	@Override
	public List<Object> getList(String key) {
		return getListByRange(key, 0, -1);
	}
	/**
	 * 获取list长度
	 * @param key
	 * @return
	 */
	@Override
	public Long getListSize(String key) {  
        return listOps.size(getUniqueKey(key));  
    }  
	/**
	 * 按范围索引
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	@Override
	public List<Object> getListByRange(String key, int start, int end){
		String uniqueKey = getUniqueKey(key);
		List<Object> list = listOps.range(uniqueKey, start, end);
		return list;
	}
	/**
	 * 按索引赋值
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	@Override
	public boolean setByIndex(String key, long index, Object value){
		listOps.set(getUniqueKey(key), index, value);	
		return true;
	}
	/**
	 * 按索引取值
	 * @param key
	 * @param indexredisTemplate.opsForSet()
	 * @return
	 */
	@Override
	public Object getByIndex(String key, long index){
		return listOps.index(getUniqueKey(key), index);
	}
	
	/**
	 * 按索引删除
	 */
	@Override
	public boolean deleteByIndex(String key, long index, Object value){
		listOps.remove(getUniqueKey(key), index, value);
		return true;
	}
	
	/** --------------------->> set操作 <<--------------------- */
	/**
	 * 存set
	 */
	@Override
	public boolean addSet(String key, Set<Object> set){
		String uniqueKey = getUniqueKey(key);
		for(Object value: set)
			setOps.add(uniqueKey, value);
		return true;
	}
	/**
	 * 取set
	 */
	@Override
	public Set<Object> getSet(String key){
		Set<Object> set = setOps.members(getUniqueKey(key));
		return set;
	}
	/**
	 * 获取set长度
	 */
	@Override
	public Long getSetSize(String key){
		return setOps.size(getUniqueKey(key));
	}
	/**
	 * 删除指定key中的set集合的某个元素
	 */
	@Override
	public boolean deleteElement(String key, Object value){
		return setOps.remove(getUniqueKey(key), value);
	}
	/**
	 * 判断指定key中的set集合是否包含某元素
	 */
	@Override
	public boolean isMemBer(String key, Object value){
		return setOps.isMember(getUniqueKey(key), value);
	}
	
	/** --------------------->> map操作 <<--------------------- */
	public boolean putAll(String key, Map<Object, Object> map){
		hashOps.putAll(getUniqueKey(key), map);
		return true;
	}
	public Map<Object, Object> getMap(String key){
		return hashOps.entries(getUniqueKey(key));
	}
	
	/** --------------------->> zset操作 <<--------------------- */
	
	/** --------------------->> 通用操作 <<--------------------- */
	/**
	 * 删除
	 */
	@Override
	public boolean delete(String key) {
		valueOps.getOperations().delete(getUniqueKey(key));
		return true;
	}
	/**
	 * 清空缓存
	 */
	@Override
	public boolean clear() {
		return redisTemplate.execute(new RedisCallback<Object>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "okay";
            }
        }).equals("okay");
	}
	/**
     * make unique key by prepend cache name
     * @param key: logic key
     * @return the unique key with cache name
     */	
    private String getUniqueKey(Object key) {
        return new StringBuilder().append(this.appName).append("#").append(String.valueOf(key)).toString();
    }
}
