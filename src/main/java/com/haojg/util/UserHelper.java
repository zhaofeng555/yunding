package com.haojg.util;

import javax.servlet.http.HttpServletRequest;

import com.haojg.model.User;

public class UserHelper {

	
	private static final String USER_SESSION_KEY = "user";

	public static boolean isAdmin(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute(USER_SESSION_KEY);
		return (user != null && user.getRole()==1);
	}

	public static void setCurrentUser(HttpServletRequest request, User user) {
		request.getSession().setAttribute(USER_SESSION_KEY, user);
		request.getSession().setAttribute("isAdmin", isAdmin(request));
	}
	public static void removeCurrentUser(HttpServletRequest request) {
		request.getSession().removeAttribute(USER_SESSION_KEY);
	}
	
	public static User getCurrentUser(HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute(USER_SESSION_KEY);
		return user;
	}
	
}
