package com.yunda.rediscache.cache;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yunda.rediscache.User;
import com.yunda.rediscache.dao.IRedisCache;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class RedisCacheTest {
	@Autowired
	public IRedisCache redisCache;
	
	private User user1;
	private User user2;
	private String test = "test";
	private List<Object> list = new ArrayList<Object>();
	private Set<Object> set = new HashSet<Object>();
	private Map<Object, Object> map =new HashMap<Object, Object>();
	@Before
	public void setUp(){
		user1 = new User("zhengfc","male",10,"shanghai");
		user2 = new User("youzx","male",8,"sh");
		list.add(test);
		list.add(user1);
		list.add(user2);
		set.add(test);
		set.add(user1);
		set.add(user2);
		map.put(test, test);
		map.put("user1", user1);
		map.put("user2", user2);
	}
	
	/** ---------------------->>pojo测试<<---------------------- */
	@Test
	public void testSet(){
		set();
		redisCache.set(test, user1);
		assertEquals(user1.getAddress(),((User)(redisCache.get(test))).getAddress());
	}
	
	@Test
	public void testGet(){
		set();
		assertEquals(user1.getAge(),((User)(redisCache.get("zhengfc"))).getAge());
		assertEquals(user2.getAddress(),((User)(redisCache.get("youzx"))).getAddress());
		assertTrue(redisCache.get(test).equals(test));
	}
	@Test
	public void testDelete(){
		set();
		redisCache.delete("zhengfc");
		assertNull(redisCache.get("zhengfc"));
		assertEquals(user2.getSex(),((User)(redisCache.get("youzx"))).getSex());
	}
	@Test
	public void testClear(){
		set();
		redisCache.clear();
		assertNull(redisCache.get("zhengfc"));
		assertNull(redisCache.get(test));
		assertNull(redisCache.get("youzx"));
	}
	
	/** ---------------------->>list测试<<---------------------- */
	@Test
	public void testSetList(){
		setList();
	}
	@Test
	public void testSize(){
		redisCache.clear();
		setList();
		assertEquals(3, redisCache.getListSize("list").intValue());
	}
	@Test 
	public void testGetList(){
		redisCache.clear();
		setList();
		List<Object> actual = redisCache.getList("list");
		assertEquals(test,actual.get(0).toString());
		assertEquals(user1.getAddress(),((User)actual.get(1)).getAddress());
		assertEquals(user2.getAge(),((User)actual.get(2)).getAge());
	}
	@Test 
	public void testDeleteList(){
		redisCache.clear();
		setList();
		redisCache.delete("list");
		assertNull(redisCache.get("list"));
	}
	@Test
	public void testGetListByRange(){
		redisCache.clear();
		setList();
		List<Object> actual = redisCache.getListByRange("list", 1, 3);
		assertEquals(user1.getAddress(),((User)actual.get(0)).getAddress());
		assertEquals(user2.getAge(),((User)actual.get(1)).getAge());
	}
	@Test
	public void testSetGetByIndex(){
		redisCache.clear();
		setList();
		redisCache.setByIndex("list", 1, test);
		assertEquals(test, redisCache.getByIndex("list", 1));
	}
	@Test
	public void testDeleteByIndex(){
		redisCache.clear();
		setList();
		redisCache.deleteByIndex("list", 1, user1);
		assertEquals(redisCache.getListSize("list").intValue(), 2);
		assertEquals(redisCache.getByIndex("list", 0), test);
		assertEquals(((User)(redisCache.getByIndex("list", 1))).getAddress(), user2.getAddress());
	}
	@Test
	public void testSetSize(){
		redisCache.clear();
		addSet();
		int actual = redisCache.getSetSize("set").intValue();
		assertEquals(3,actual);
	}
	@Test
	public void testGetSet(){
		redisCache.clear();
		addSet();
		Set<Object> actual = redisCache.getSet("set");
		for(Object value: actual){
			System.out.print("--------------testDeleteElement--->>");
			if(value.getClass().isInstance(user1))
				System.out.println(((User)value).getName());
			else
				System.out.println(value);
		}
		assertTrue(actual.contains(test));
	}
	@Test
	public void testDeleteElement(){
		redisCache.clear();
		addSet();
		redisCache.deleteElement("set", user1);
		Set<Object> actual = redisCache.getSet("set");
		for(Object value: actual){
			System.out.print("--------------testDeleteElement--->>");
			if(value.getClass().isInstance(user1))
				System.out.println(((User)value).getName());
			else
				System.out.println(value);
		}
		assertTrue(actual.contains(test));
		assertTrue(actual.size()==2);
	}
	@Test
	public void testIsMember(){
		redisCache.clear();
		addSet();
		assertTrue(redisCache.isMemBer("set", user1));
		assertTrue(redisCache.isMemBer("set", user2));
		assertTrue(redisCache.isMemBer("set", test));
	}
	@Test
	public void testMap(){
		redisCache.clear();
		put();
		Map<Object, Object> map = redisCache.getMap("map");
		assertTrue(map.get(test).equals(test));
		assertTrue(((User)map.get("user1")).getName().equals("zhengfc"));
		assertTrue(((User)map.get("user2")).getName().equals("youzx"));
	}
//	@Test
//	public void testPerformace(){
//		redisCache.clear();
//		User userp = new User("zhengfc","male",10,"shanghai");
//		List<Object> listp = new ArrayList<Object>();
//		for(int i=0; i<1000000; i++)
//			listp.add(userp);
//		long start = System.currentTimeMillis();
//		redisCache.setList("listp", listp);
//		long middle = System.currentTimeMillis();
//		redisCache.getList("listp");
//		long end = System.currentTimeMillis();
//		System.out.println("time-----set------>>"+(middle-start));
//		System.out.println("time-----get------>>"+(end-middle));
//	}
	@After
	public void tearDown() {
		redisCache.clear();
	}
	
	private void set(){
		redisCache.set("zhengfc", user1);
		redisCache.set("youzx", user2);
		redisCache.set(test, test);
	}
	private void setList(){
		redisCache.setList("list", list);
	}
	private void addSet(){
		redisCache.addSet("set", set);
	}
	private void put(){
		redisCache.putAll("map", map);
	}
}
