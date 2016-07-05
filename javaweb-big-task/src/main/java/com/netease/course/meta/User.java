package com.netease.course.meta;

public class User {
	private Integer id;
	private String username;
	private String password;
	private String nickName;
	private Integer usertype;
	
	public User(String username, Integer usertype) {
		this.username = username;
		this.usertype = usertype;
	}
	public User() {
	}
	
	public Integer getUsertype() {
		return usertype;
	}
	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
