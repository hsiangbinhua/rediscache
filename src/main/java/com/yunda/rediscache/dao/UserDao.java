package com.yunda.rediscache.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.yunda.rediscache.cache.User;



/**
 * @author zhengfc
 * 测试@cache用
 */
public class UserDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	private Map<String, User> mockDB = new ConcurrentHashMap<String, User>();
	
	@CacheEvict(value="user", allEntries=true)
	public boolean save(User user){
		logger.info("excute save user:id="+user.getId());
		mockDB.put(user.getId(), user);
		return true;
	}

	@CacheEvict(value = "user", allEntries = true)
	public boolean update(User user){
		logger.info("excute update user:id="+user.getId());
		if(!mockDB.containsKey(user.getId()))
			return false;
		mockDB.put(user.getId(), user);
		return true;
	}
	@CacheEvict(value = "user", allEntries = true)
	public boolean delete(String id){
		logger.info("excute delete user:id="+id);
		if(!mockDB.containsKey(id))
			return false;
		mockDB.remove(id);
		return true;
	}
	@Cacheable(value="user")
	public User getById(String id){
		logger.info("excute getById:id="+id);
		if(!mockDB.containsKey(id))
			return null;
		return mockDB.get(id);
	}
	@Cacheable("user")
	public List<User> getAll(){
		logger.info("excute getAll");
		List<User> ret = new ArrayList<User>();
		ret.addAll(mockDB.values());
		return ret;
	}
	
}
