package com.netease.course.dao;


import com.netease.course.meta.User;

public interface UserDao {
	
	public User getUser(String userName,String password);
}
