package com.yunda.rediscache.cache;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yunda.rediscache.dao.UserDao;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class CacheableTest{
	@Autowired
	public RedisCache redisCache;
	private User user1;
	private User user2;
	private List<User> expectList = new ArrayList<User>();
	@Resource
	private UserDao userDao;
	@Before
	public void setUp(){
		user1 = new User("id1","zhengfc","male",30,"jdz");
		user2 = new User("id2","youzx","male",20,"yc");
		expectList.add(user1);
		expectList.add(user2);
	}
	@Test
	public void testCacheable(){
		userDao.save(user1);
		userDao.save(user2);
		User first = userDao.getById("id1");
		assertEquals(first, user1);
		User second = userDao.getById("id1");
		assertEquals(first.getId(),second.getId());
		List<User> firstAll = userDao.getAll();
		for(int i=0; i<firstAll.size(); i++)
			assertEquals(firstAll.get(i).getId(), expectList.get(i).getId());
		List<User> secondAll = userDao.getAll();
		for(int i=0; i<firstAll.size(); i++)
			assertEquals(firstAll.get(i).getId(), secondAll.get(i).getId());
	}
	@After
	public void tearDown(){
		redisCache.clear();
	}
}
