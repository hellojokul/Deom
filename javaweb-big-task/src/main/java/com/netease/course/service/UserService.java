package com.netease.course.service;

import com.netease.course.meta.User;

public interface UserService {
	
	public User getUser(String userName, String password);
	
	public User getUser(String nickName);
}
