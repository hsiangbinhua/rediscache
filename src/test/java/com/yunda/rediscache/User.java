package com.yunda.rediscache;

import java.io.Serializable;

public class User implements Serializable{
	private static final long serialVersionUID = 4682848895237227837L;
	private String name,sex,address;
	private int age;
	 
	public User(){
	}
	public User(String name, String sex, int age, String address){
		this.name = name;
		this.sex = sex;
		this.age = age;
		this.address = address;
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
}
