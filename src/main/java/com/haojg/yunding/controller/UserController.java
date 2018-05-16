package com.haojg.yunding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
