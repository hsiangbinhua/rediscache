package com.yunda.rediscache.cache;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 4682848895237227837L;
	private String id,name,sex,address;
	private int age;
	 
	public User(){
	}
	public User(String id, String name, String sex, int age, String address){
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.address = address;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }
}
