package com.yunda.rediscache.utils.serializer;

import java.util.List;
import java.util.Set;


/**
 * 序列化
 * @author zhengfc 
 */
public interface Serializer {
    public String toString(Object object);

    public Object toObject(String string);
    
    public List<Object> deSerializeList(List<String> list);
    
    public Set<Object> deSerializeSet(Set<Object> set);
}
