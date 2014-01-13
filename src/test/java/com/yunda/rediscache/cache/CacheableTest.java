package com.yunda.rediscache.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yunda.rediscache.dao.UserDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class CacheableTest{
	private User user1;
	private User user2;
	private User user3;
	private List<User> expectList = new ArrayList<User>();
	@Resource
	private UserDao userDao;
	@Before
	public void setUp(){
		user1 = new User("id1","zhengfc","male",30,"jdz");
		user2 = new User("id2","youzx","male",20,"yc");
		user3 = new User("id3","zhouj","male",20,"hn");
		expectList.add(user1);
		expectList.add(user2);
	}
	/**
	 * 控制台通过log查看运行是否走缓存
	 */
	@Test
	public void testCaching(){
		System.out.println("cachable------------------------------------------------------->>");
		userDao.save(user1);
		userDao.save(user2);
		User first = userDao.getById("id1");
		assertEquals(first, user1);
		User second = userDao.getById("id1");
		assertEquals(first, second);
		userDao.getAll();
		userDao.getAll();
		//增
		System.out.println("save------------------------------------------------------->>");
		userDao.save(user3);
		assertEquals(userDao.getById("id1"),user1);
		userDao.getAll();
		userDao.getAll();
		//删
		System.out.println("delete------------------------------------------------------->>");
		userDao.delete("id1");
		assertNull(userDao.getById("id1"));
		assertEquals(userDao.getById("id2"),user2);
		userDao.getById("id2");
		//改
		System.out.println("update------------------------------------------------------->>");
		user2.setSex("female");
		userDao.update(user2);
		assertEquals(userDao.getById("id2"),user2);
		userDao.getById("id1");
		assertEquals(userDao.getById("id2").getSex(),"female");
	}
	@After
	public void tearDown(){
		//redisDao.clear();
	}
}
