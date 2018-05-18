package com.haojg.shouji.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.haojg.shouji.bean.User;

public class UserHelper {

	
	public static boolean isAdmin(HttpSession session) {
		User user = (User)session.getAttribute("user");
		return (user != null && user.getRole()==0);
	}
	

	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("user");
		return user;
	}
	
	
	
}
