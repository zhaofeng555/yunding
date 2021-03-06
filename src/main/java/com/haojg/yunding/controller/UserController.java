package com.haojg.yunding.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
public class UserController{

	@Autowired
	UserService service;
	
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
		
		if(user.getState()==0){
			return "redirect:/login.html?lock=2";
		}
		
		
		UserHelper.setCurrentUser(request, user);
		
//		return OutpubResult.getSuccess("注册成功");
//		return "redirect:/user/admin";
		return "redirect:/goto/company";
	}
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpSession session, HttpServletRequest request){
		UserHelper.removeCurrentUser(request);
		return "redirect:/login.html";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(HttpSession session, HttpServletRequest request){
		
		return "admin";
	}

	@RequestMapping(value="/getUserInfoByUserName", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult getUserInfoById(String username, HttpServletRequest request) {

		User data = service.findByUsername(username);
		if(data != null){
			User userInfo = new User();
			userInfo.setId(data.getId());
			userInfo.setMobile(data.getMobile());
			userInfo.setRealname(data.getRealname());
			userInfo.setUsername(data.getUsername());
			return OutpubResult.getSuccess(userInfo);
		}
		return OutpubResult.getError("不存在此用户");
	}
	
	@RequestMapping(value="/getById", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult get(Long id, HttpServletRequest request) {
		if(!UserHelper.isAdmin(request)){
			return OutpubResult.getError("权限不足");
		}
		 User data = getService().getOne(id);
		return OutpubResult.getSuccess(data);
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(User user, HttpServletRequest request){
		
		User recUser = UserHelper.getCurrentUser(request);
		recUser=service.getOne(recUser.getId());
		Double buyNum = user.getBuyNum();
		
		Double recBuyNum = recUser.getBuyNum();
		Double recAssets = recUser.getAssets();
		
		String recArea = recUser.getArea();
		String area = user.getArea();
		
		String cardId = user.getCardId();
		if(StringUtils.isBlank(cardId)){
			return OutpubResult.getError("身份证号为空");
		}
		String mobile = user.getMobile();
		if(StringUtils.isBlank(mobile)){
			return OutpubResult.getError("手机号为空");
		}
		
		//检查cardId
		Example countUserByCardIdEx = new Example(User.class);
		countUserByCardIdEx.createCriteria().andEqualTo("cardId", cardId);
		Integer ctByCardId = service.count(countUserByCardIdEx);
		if(ctByCardId > 0){
			return OutpubResult.getError("该身份证号已经被注册");
		}
		
		//
		Example countUserByMobileEx = new Example(User.class);
		countUserByMobileEx.createCriteria().andEqualTo("mobile", mobile);
		Integer ctByMobile = service.count(countUserByMobileEx);
		if(ctByMobile > 0){
			return OutpubResult.getError("该手机号号已经被注册");
		}
		
		if(!UserHelper.isAdmin(request) && !StringUtils.endsWith(recArea, area)) {
			return OutpubResult.getError("地区权限不足");
		}
		
		
		if((recBuyNum+recAssets) < buyNum) {
			// error
			return OutpubResult.getError("资产不足");
		}
		
		try {
			user.setCreateTime(new Date());
			user.setUpdateTime(new Date());
			user.setRecUserId(recUser.getId());
			user.setState(1);
			service.insertSelective(user);
			
			//扣除推荐人的数字资产
			/*
			if(recBuyNum >= buyNum){
				recUser.setBuyNum(recBuyNum-buyNum);
			}else{
				recUser.setBuyNum(0D);
				recUser.setAssets(recAssets - (buyNum-recBuyNum));
			}
			*/
			//优先扣除浮动资产
			if(recAssets >= buyNum){
				recUser.setAssets(recAssets - buyNum);
			}else{
				recUser.setAssets(0D);
				recUser.setBuyNum(recBuyNum - (buyNum-recAssets));
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

		User recUser = getService().getOne(curUser.getRecUserId());
		map.put("recUser", recUser);

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
	
	@RequestMapping(value="/changePasswd", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult changePasswd(String oldPassword, String newPassword,  HttpServletRequest request){
		if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)){
			return OutpubResult.getError("密码不能为空");
		}
		User user = UserHelper.getCurrentUser(request);
		user = getService().getOne(user.getId());
		
		String password = user.getPassword();
		if(StringUtils.equals(password, oldPassword)){
			User updateUser = new User();
			updateUser.setId(user.getId());
			updateUser.setPassword(newPassword);
			updateUser.setUpdateTime(new Date());
			int ct = service.updateByPrimaryKeySelective(updateUser);
			if(ct >= 0 ){
				return OutpubResult.getSuccess("修改密码成功");
			}
		}
		
		return OutpubResult.getError("修改密码失败");
	}
	
	@RequestMapping(value="/changeSecondPasswd", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult changeSecondPasswd(String oldPassword, String newPassword,  HttpServletRequest request){
		if(StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)){
			return OutpubResult.getError("密码不能为空");
		}
		User user = UserHelper.getCurrentUser(request);
		user = getService().getOne(user.getId());
		
		String password = user.getSecondPassword();
		if(StringUtils.equals(password, oldPassword)){
			User updateUser = new User();
			updateUser.setId(user.getId());
			updateUser.setSecondPassword(newPassword);
			updateUser.setUpdateTime(new Date());
			int ct = service.updateByPrimaryKeySelective(updateUser);
			if(ct >= 0 ){
				return OutpubResult.getSuccess("修改密码成功");
			}
		}
		
		return OutpubResult.getError("修改密码失败");
	}
	
	
	@RequestMapping(value="/login2", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult login2(String secondPassword, HttpServletRequest request){
		User user = UserHelper.getCurrentUser(request);
		
		String password = user.getSecondPassword();
		if(StringUtils.equals(password, secondPassword)){
			return OutpubResult.getSuccess("成功");
		}
		
		return OutpubResult.getError("失败");
		
		
	}
	
	@RequestMapping(value="/validate", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult validate(HttpServletRequest request){
		User user = UserHelper.getCurrentUser(request);
		
		if(user == null ){
			return OutpubResult.getSuccess("成功");
		}
		
		return OutpubResult.getError("失败");
		
		
	}
	
	@RequestMapping(value="/check", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult check(Long userId, Integer state, HttpServletRequest request){
		
		
		boolean admin = UserHelper.isAdmin(request);
		
		if(admin){
			User updateUser = new User();
			updateUser.setId(userId);
			updateUser.setState(state);
			int ct = service.updateByPrimaryKeySelective(updateUser);
			if(ct>0){
				return OutpubResult.getSuccess("成功");
			}
		}else{
			return OutpubResult.getError("权限不足");
		}
		return OutpubResult.getError("失败");
	}
	

	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult save(User record, HttpServletRequest request) throws Exception {
		User currentUser = UserHelper.getCurrentUser(request);
		
		User updateUser = new User();
		if(currentUser.getId().equals(record.getId())){
			updateUser.setId(record.getId());
			updateUser.setRealname(record.getRealname());
			updateUser.setCardId(record.getCardId());
			updateUser.setBankName(record.getBankName());
			updateUser.setBankNo(record.getBankNo());
			updateUser.setMail(record.getMail());
			updateUser.setMobile(record.getMobile());
		}else if(UserHelper.isAdmin(request)){
			BeanUtils.copyProperties(record, updateUser);
		}else{
			return OutpubResult.getError("权限不足");
		}
		
		
		
		
		int ct = getService().saveOrUpdate(updateUser);
		
		if(ct == 0) {
			return OutpubResult.getError("保存失败");
		}
		return OutpubResult.getSuccess(updateUser);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult delete(Long id, HttpServletRequest request) {
		if(!UserHelper.isAdmin(request)){
			return OutpubResult.getError("权限不足");
		}
		int ct = getService().delete(id);
		if(ct == 0) {
			return OutpubResult.getError("删除失败");
		}
		return OutpubResult.getSuccess(id);
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	@ResponseBody
	public OutpubResult list(
			@RequestParam(required=false, defaultValue="1")Integer pageNum, 
			@RequestParam(required=false, defaultValue="15")Integer pageSize, 
			HttpServletRequest request) {
		if(!UserHelper.isAdmin(request)){
			return OutpubResult.getError("权限不足");
		}
		List<User> list = getService().getPageList(pageNum, pageSize);
		return OutpubResult.getSuccess(list);
	}
	
	@RequestMapping(value="/listing", method=RequestMethod.GET)
	public String list(
			@RequestParam(required=false, defaultValue="1")Integer pageNum, 
			@RequestParam(required=false, defaultValue="15")Integer pageSize,
			ModelMap map, HttpServletRequest request) {
		
		if(!UserHelper.isAdmin(request)){
			return "error";
		}
		String simpleClassName = User.class.getSimpleName().toLowerCase();
		List<User> list = getService().getPageList(pageNum, pageSize);
		
		Integer sum = getService().count();
		Integer total = sum/pageSize;
		if(sum%pageSize != 0){
			++total;
		}
		
		map.put("data", list);
		map.put("pageNum", pageNum);
		map.put("total", total);
		map.put("isFirst", (pageNum<=1));
		map.put("isLast", (pageNum>=total));
		map.put("pageSize", pageSize);
		return simpleClassName+"/list";
	}
}
