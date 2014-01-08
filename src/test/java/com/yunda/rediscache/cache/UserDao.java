package com.yunda.rediscache.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public class UserDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	private Map<String, User> mockDB = new ConcurrentHashMap<String, User>();
	public boolean save(User user){
		logger.info("excute save user:id="+user.getId());
		mockDB.put(user.getId(), user);
		return true;
	}
	@CacheEvict(value = "user", key = "id")
	public boolean update(User user){
		logger.info("excute update user:id="+user.getId());
		if(!mockDB.containsKey(user.getId()))
			return false;
		mockDB.put(user.getId(), user);
		return true;
	}
	@CacheEvict(value = "user", key = "id")
	public boolean delete(String id){
		logger.info("excute delete user:id="+id);
		if(!mockDB.containsKey(id))
			return false;
		mockDB.remove(id);
		return true;
	}
	@Cacheable(value = "user", key = "id")
	public User getById(String id){
		logger.info("excute getById:id="+id);
		if(!mockDB.containsKey(id))
			return null;
		return mockDB.get(id);
	}
	@Cacheable("users")
	public List<User> getAll(){
		logger.info("excute getAll");
		return (List<User>)mockDB.values();
	}
	
}
