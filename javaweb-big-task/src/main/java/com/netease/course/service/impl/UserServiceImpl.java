package com.netease.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netease.course.dao.UserDao;
import com.netease.course.meta.User;
import com.netease.course.service.UserService;

@Component
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User getUser(String userName, String password) {
		User user = userDao.getUser(userName, password);
		if(user!=null) {
			user.setPassword(null);
		}
		return user;
	}

	@Override
	public User getUser(String nickName) {
		return userDao.getUserByType(nickName);
	}
}
