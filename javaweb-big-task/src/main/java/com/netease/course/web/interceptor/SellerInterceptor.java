package com.netease.course.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.netease.course.meta.User;

public class SellerInterceptor extends HandlerInterceptorAdapter{

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		User user = (User)request.getSession().getAttribute("user");
		if(user!=null && user.getNickName().equals("seller")) {
			return true;
		}
		request.getRequestDispatcher("/login").forward(request, response);
		return false;
	}
	
}
