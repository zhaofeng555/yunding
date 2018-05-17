package com.haojg.yunding.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojg.controller.BaseController;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
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
	

	@RequestMapping(value="/me", method=RequestMethod.GET)
	public String get(ModelMap map, HttpSession session) {
//		Object id = session.getAttribute("userId");
		Long id = new Long(1L);
		User data = getService().getOne(id);
		map.put("data", data);
		return "userInfo";
	}
	
	
}
