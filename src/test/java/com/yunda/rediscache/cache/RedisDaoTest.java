package com.yunda.rediscache.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import com.yunda.rediscache.dao.RedisDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-test.xml")
public class RedisDaoTest{
	@Autowired
	public RedisDao redisDao;
	
	private User user1;
	private User user2;
	private String test = "test";
	private List<Object> list = new ArrayList<Object>();
	private Set<Object> set = new HashSet<Object>();
	private Map<Object, Object> map =new HashMap<Object, Object>();
	@Before
	public void setUp(){
		user1 = new User("id1","zhengfc","male",10,"shanghai");
		user2 = new User("id2","youzx","male",8,"sh");
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
		redisDao.set(test, user1);
		assertEquals(user1,(User)(redisDao.get(test)));
	}
	
	@Test
	public void testGet(){
		set();
		assertEquals(user1,(User)(redisDao.get("zhengfc")));
		assertEquals(user2,(User)(redisDao.get("youzx")));
		assertTrue(redisDao.get(test).equals(test));
	}
	@Test
	public void testDeletePojo(){
		set();
		redisDao.delete("zhengfc");
		assertNull(redisDao.get("zhengfc"));
		assertEquals(user2,(User)(redisDao.get("youzx")));
	}
	@Test
	public void testclear(){
		set();
		redisDao.clear();
		assertNull(redisDao.get("zhengfc"));
		assertNull(redisDao.get(test));
		assertNull(redisDao.get("youzx"));
	}
	
	/** ---------------------->>list测试<<---------------------- */
	@Test
	public void testSetList(){
		setList();
	}
	@Test
	public void testSize(){
		redisDao.clear();
		setList();
		assertEquals(3, redisDao.getListSize("list").intValue());
	}
	@Test 
	public void testGetList(){
		redisDao.clear();
		setList();
		List<Object> actual = redisDao.getList("list");
		assertEquals(test,actual.get(0));
		assertEquals(user1,(User)actual.get(1));
		assertEquals(user2,(User)actual.get(2));
	}
	@Test 
	public void testDeleteList(){
		redisDao.clear();
		setList();
		redisDao.delete("list");
		assertNull(redisDao.get("list"));
	}
	@Test
	public void testGetListByRange(){
		redisDao.clear();
		setList();
		List<Object> actual = redisDao.getListByRange("list", 1, 3);
		assertEquals(user1,(User)actual.get(0));
		assertEquals(user2,(User)actual.get(1));
	}
	@Test
	public void testSetGetByIndex(){
		redisDao.clear();
		setList();
		redisDao.setByIndex("list", 1, test);
		assertEquals(test, redisDao.getByIndex("list", 1));
	}
	@Test
	public void testDeleteByIndex(){
		redisDao.clear();
		setList();
		redisDao.deleteByIndex("list", 1, user1);
		assertEquals(redisDao.getListSize("list").intValue(), 2);
		assertEquals(redisDao.getByIndex("list", 0), test);
		assertEquals((User)(redisDao.getByIndex("list", 1)), user2);
	}
	@Test
	public void testSetSize(){
		redisDao.clear();
		addSet();
		int actual = redisDao.getSetSize("set").intValue();
		assertEquals(3,actual);
	}
	@Test
	public void testGetSet(){
		redisDao.clear();
		addSet();
		Set<Object> actual = redisDao.getSet("set");
		for(Object value: actual)
			if(value.getClass().isInstance(user1))
				assertTrue(user1.equals((User)value)|| user2.equals((User)value));
		assertTrue(actual.contains(test));
	}
	@Test
	public void testDeleteElement(){
		redisDao.clear();
		addSet();
		redisDao.deleteElement("set", user1);
		Set<Object> actual = redisDao.getSet("set");
		for(Object value: actual){
			if(value.getClass().isInstance(user1))
				assertEquals(user2,(User)value);
		}
		assertTrue(actual.contains(test));
		assertTrue(actual.size()==2);
	}
	@Test
	public void testIsMember(){
		redisDao.clear();
		addSet();
		assertTrue(redisDao.isMemBer("set", user1));
		assertTrue(redisDao.isMemBer("set", user2));
		assertTrue(redisDao.isMemBer("set", test));
	}
	@Test
	public void testDeleteSet(){
		redisDao.clear();
		addSet();
		redisDao.delete("set");
		assertNull(redisDao.getSet("set"));
	}
	@Test
	public void testMap(){
		redisDao.clear();
		put();
		Map<Object, Object> map = redisDao.getMap("map");
		assertTrue(map.get(test).equals(test));
		assertEquals(user1, (User)map.get("user1"));
		assertEquals(user2, (User)map.get("user2"));;
	}
	@Test
	public void testHasKey(){
		redisDao.clear();
		put();
		assertTrue(redisDao.hasKey("map"));
	}
	@Test
	public void testDeleteMap(){
		redisDao.clear();
		put();
		redisDao.delete("map");
		assertNull(redisDao.getMap("map"));
	}
//	@Test
//	public void testPerformace(){
//		redisDao.clear();
//		User userp = new User("zhengfc","male",10,"shanghai");
//		List<Object> listp = new ArrayList<Object>();
//		for(int i=0; i<1000000; i++)
//			listp.add(userp);
//		long start = System.currentTimeMillis();
//		redisDao.setList("listp", listp);
//		long middle = System.currentTimeMillis();
//		redisDao.getList("listp");
//		long end = System.currentTimeMillis();
//		System.out.println("time-----set------>>"+(middle-start));
//		System.out.println("time-----get------>>"+(end-middle));
//	}
	@After
	public void tearDown() {
		redisDao.clear();
	}
	
	private void set(){
		redisDao.set("zhengfc", user1);
		redisDao.set("youzx", user2);
		redisDao.set(test, test);
	}
	private void setList(){
		redisDao.setList("list", list);
	}
	private void addSet(){
		redisDao.addSet("set", set);
	}
	private void put(){
		redisDao.putAll("map", map);
	}
}
