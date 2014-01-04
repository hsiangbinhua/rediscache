package com.yunda.rediscache.utils.serializer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 使用fastjson实现
 * @author zhengfc
 */
public class JsonSerializer implements Serializer {

    private static final String CLASS_OBJECT_BREAKER = "@@";

    @Override
    public String toString(Object object) {
        return object == null ? null : new StringBuilder()
                .append(object.getClass().getName())
                .append(CLASS_OBJECT_BREAKER)
                .append(JSON.toJSONString(object, SerializerFeature.WriteClassName))
                .toString();
    }

    @Override
    public Object toObject(String string){
        try{
        	if (string != null && string.trim().length() != 0) {
        		String[] classObjectPairs = string.split(CLASS_OBJECT_BREAKER, 2);
    	        if (classObjectPairs.length != 2)
    	            throw new ClassNotFoundException("classObjectPairs length is not 2\n" + string);
    	        Class<?> clazz = Class.forName(classObjectPairs[0]);
    	        //noinspection unchecked
    	        return JSON.parseObject(classObjectPairs[1], clazz);
            }
            
    	}catch (ClassNotFoundException e){
    		e.getStackTrace();
    	}
        return null;
    }
    
    @Override
    public List<Object> deSerializeList(List<String> list){
    	List<Object> ret =new ArrayList<Object>();
    	for(String value: list)
    		ret.add(toObject(value));
    	return ret;
    }
    
    @Override
    public Set<Object> deSerializeSet(Set<Object> set){
    	Set<Object> ret = new HashSet<Object>();
    	for(Object value: set)
    		ret.add(toObject(value.toString()));
    	return ret;
    }
}
