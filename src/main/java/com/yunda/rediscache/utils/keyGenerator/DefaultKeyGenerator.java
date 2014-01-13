package com.yunda.rediscache.utils.keyGenerator;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;


public class DefaultKeyGenerator implements KeyGenerator{

	@Override
	public Object generate(Object arg0, Method arg1, Object... arg2) {
		StringBuilder builder = new StringBuilder();
		builder.append(arg0.getClass().getName()).append("#").append(arg1.getName());
		if(arg2!=null && arg2.length!=0)
			for(int i=0;i<arg2.length;i++)
				builder.append(".").append(arg2[i]);
		System.out.println("key------------>>"+builder.toString());
		return builder;
	}
	
	public String generateSimpleKey(Object key){
		return key.toString().trim();
	}
}
