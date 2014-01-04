package com.yunda.rediscache.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * rediscache对外接口
 * 应用程序一致通过此接口调用缓存
 * 此接口对应的缓存实现类为单例类
 */
public interface IRedisCache {
	/** --------------------->>pojo操作<<--------------------- */
	boolean set(String key, Object obj); //添加和更新为同一接口,redis中含key为更新，不含为添加
	Object get(String key);
	
	/** --------------------->>list操作<<--------------------- */
	boolean setList(String key, List<Object> list);
	List<Object> getList(String key);
	Long getListSize(String key);  //list长度
	List<Object> getListByRange(String key, int start, int end);  //按范围索引
	boolean setByIndex(String key, long index, Object value);  //按索引赋值
	Object getByIndex(String key, long index);  //按索引取值
	boolean deleteByIndex(String key, long index, Object value);  //按索引删除(list里数据可重复,所以需要给值)
	
	/** --------------------->>set操作 <<--------------------- */
	boolean addSet(String key, Set<Object> set);
	Set<Object> getSet(String key);
	Long getSetSize(String key);
	boolean deleteElement(String key, Object value);
	boolean isMemBer(String key, Object value);
	
	/** --------------------->>map操作 <<--------------------- */
	boolean putAll(String key, Map<Object, Object> map);
	Map<Object, Object> getMap(String key);
	
	/** --------------------->>通用操作 <<--------------------- */
	//删除
	boolean delete(String key);
	//清空缓存
	boolean clear();
}
