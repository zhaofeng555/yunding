package com.haojg.yunding.controller;

import java.util.Date;
import java.util.List;

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

import tk.mybatis.mapper.entity.Example;

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
//	@ResponseBody
	public String login(String username, String password, String verifyCode, HttpServletRequest request){
		
		User user = service.login(username, password);
		if(user ==null) {
			return "redirect:/login.html?error=1";
//			return OutpubResult.getError("用户名或密码错误");
		}
		
		UserHelper.setCurrentUser(request, user);
		
//		return OutpubResult.getSuccess("注册成功");
		return "redirect:/user/admin";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(HttpSession session, HttpServletRequest request){
		
		return "admin";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(User user, HttpServletRequest request){
		
		User recUser = UserHelper.getCurrentUser(request);
		recUser=service.getOne(recUser.getId());
		Integer buyNum = user.getBuyNum();
		
		Integer recBuyNum = recUser.getBuyNum();
		Double recAssets = recUser.getAssets();
		
		
		if((recBuyNum+recAssets) < buyNum) {
			// error
			return OutpubResult.getError("资产不足");
		}
		
		try {
			user.setUpdateTime(new Date());
			user.setRecUserId(recUser.getId());
			service.insertSelective(user);
			
			//扣除推荐人的数字资产
			if(recBuyNum >= buyNum){
				recUser.setBuyNum(recBuyNum-buyNum);
			}else{
				recUser.setBuyNum(0);
				recUser.setAssets(recAssets - (buyNum-recBuyNum));
			}
			service.updateByPrimaryKeySelective(recUser);
			
		} catch (Exception e) {
			e.printStackTrace();
			return OutpubResult.getError("注册失败");
		}
		
		return OutpubResult.getSuccess("注册成功");
	}
	
	@RequestMapping(value="/registerListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Example e = new Example(User.class);
		e.createCriteria().andEqualTo("recUserId", curUser.getId());
		
		List<User> userList = service.getListByExample(e);
		map.put("data", userList);
		
		return "register";
	}

	@RequestMapping(value={"/me"}, method=RequestMethod.GET)
	public String myInfo(ModelMap map,  HttpServletRequest request) {
		User user = UserHelper.getCurrentUser(request);
		User data = getService().getOne(user.getId());
		UserHelper.setCurrentUser(request, data);
		
		map.put("data", data);
		return "userInfo";
	}
	@RequestMapping(value={"/assets"}, method=RequestMethod.GET)
	public String myAssets(ModelMap map,  HttpServletRequest request) {
		User user = UserHelper.getCurrentUser(request);
		User data = getService().getOne(user.getId());
		UserHelper.setCurrentUser(request, data);
		
		map.put("data", data);
		return "myAssets";
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
