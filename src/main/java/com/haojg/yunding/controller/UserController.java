package com.haojg.yunding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.haojg.controller.BaseController;
import com.haojg.model.User;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController<User> {

	@Autowired
	UserService service;
	
	@Override
	public CustomService<User> getService() {
		return service;
	}

	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(){
		
		return "";
	}
	
}
