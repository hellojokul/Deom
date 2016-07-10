package com.netease.course.service.impl;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.netease.course.meta.User;

@Aspect
@Component
public class ServiceAspect {
	//对用户类型进行判断并根据类型决定执不执行被过滤的方法
	@Around("execution(* com.netease.course.service.impl.ProductServiceImpl.check*(..))")
	public Object checkSellerService(ProceedingJoinPoint pjp) {
		Object[] objs = pjp.getArgs();
		String type = (String)objs[objs.length-2];
		HttpSession session = (HttpSession)objs[objs.length-1];
		User user = (User)session.getAttribute("user");
		Object result = null;
		try {
			if(user!=null && user.getUsername().equals(type)) {
				result = pjp.proceed();
			}
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return result;
	}
}
