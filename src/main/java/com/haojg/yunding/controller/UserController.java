package com.haojg.yunding.controller;

import javax.servlet.http.HttpServletRequest;
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
import com.haojg.util.UserHelper;

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
	public String login(String username, String password, String verifyCode, HttpServletRequest request){
		
		User user = service.login(username, password);
		if(user ==null) {
			return "redirect:/login.html?error=1";
		}
		
		UserHelper.setCurrentUser(request, user);
		
		return "redirect:/user/admin";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(HttpSession session, HttpServletRequest request){
		
		return "admin";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(User user, HttpServletRequest request) throws Exception {
		
		User recUser = UserHelper.getCurrentUser(request);
		
		Integer buyNum = user.getBuyNum();
		Double assets = recUser.getAssets();
		if(assets < buyNum) {
			// error
			return OutpubResult.getError("资产不足");
		}
		
		recUser.setAssets(assets - buyNum);
		service.saveOrUpdate(recUser);
		
		user.setRecUserId(recUser.getId());
		service.saveOrUpdate(user);
		
		return OutpubResult.getSuccess("注册成功");
	}
	

	@RequestMapping(value="/me", method=RequestMethod.GET)
	public String myInfo(ModelMap map,  HttpServletRequest request) {
		User user = UserHelper.getCurrentUser(request);
		User data = getService().getOne(user.getId());
		UserHelper.setCurrentUser(request, data);
		
		map.put("data", data);
		return "userInfo";
	}
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult get(ModelMap map,  HttpServletRequest request) {
		User user = UserHelper.getCurrentUser(request);
		User data = getService().getOne(user.getId());
		UserHelper.setCurrentUser(request, data);
		return OutpubResult.getSuccess(data);
	}
	
	
	
	
}
