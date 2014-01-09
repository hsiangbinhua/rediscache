package com.yunda.rediscache.utils.keyGenerator;


public class CacheKeyGenerator /*implements KeyGenerator*/{

	/*@Override
	public Object generate(Object arg0, Method arg1, Object... arg2) {
		StringBuilder builder = new StringBuilder();
		builder.append(arg0).append("#")
		.append(arg1).append("#")
		.append(arg2);
		return builder;
	}*/
	
	public static String generateSimpleKey(Object key){
		return key.toString().trim();
	}
}
