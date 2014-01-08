package com.yunda.rediscache;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import com.yunda.rediscache.cache.CacheableTest;
import com.yunda.rediscache.cache.RedisDaoTest;

/**
 * Unit test for simple App.
 */
public class AppTest{

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite suite= new TestSuite("TestSuite for app");
		suite.addTest(new JUnit4TestAdapter(RedisDaoTest.class));
		suite.addTest(new JUnit4TestAdapter(CacheableTest.class));
		return suite;
	}

}
