package com.yunda.rediscache.cache;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yunda.rediscache.dao.RedisDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class CacheableTest{
	@Autowired
	public RedisDao redisCache;
	private User user1;
	private User user2;
	private UserDao userDao = new UserDao();
	@Before
	public void setUp(){
		user1 = new User("id1","zhengfc","male",30,"jdz");
		user2 = new User("id2","youzx","male",20,"yc");
	}
	@Test
	public void testCacheAble(){
		userDao.save(user1);
		userDao.save(user2);
		User first = userDao.getById("id1");
		assertEquals(first, user1);
		User second = userDao.getById("id1");
		assertEquals(first,second);
	}
	@After
	public void tearDown(){
		redisCache.clear();
	}
}
