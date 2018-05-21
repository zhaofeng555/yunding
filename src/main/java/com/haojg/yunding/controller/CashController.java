package com.haojg.yunding.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojg.controller.BaseController;
import com.haojg.model.Cash;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
import com.haojg.service.CashService;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/cash")
public class CashController extends BaseController<Cash> {

	@Autowired
	CashService service;
	
	@Autowired
	UserService userService;
	
	@Override
	public CustomService<Cash> getService() {
		return service;
	}
	
	@RequestMapping(value="/apply", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Integer num, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		
		
		Date startDateTime = curUser.getCreateTime();
		
		Date endDateTime = DateUtils.addDays(startDateTime, 180);
		
		Date curDateTime = new Date(System.currentTimeMillis());
		
		if(curDateTime.before(endDateTime)){
			return OutpubResult.getError("要过180天周期");
		}
		
		if(num%500 != 0){
			return OutpubResult.getError("提现金额必须500的整数倍");
		}
		
		curUser=userService.getOne(curUser.getId());
		
		Double assets = curUser.getAssets();
		
		if(assets<num){
			return OutpubResult.getError("资产不足");
		}
		
		Cash c = new Cash();
		c.setCreateTime(new Date());
		c.setUpdateTime(new Date());
		c.setUserId(curUser.getId());
		c.setNum(num);
		c.setState(0);
		
		service.insertSelective(c);
		
		curUser.setAssets(assets-num);
		userService.updateByPrimaryKeySelective(curUser);
		
		return OutpubResult.getSuccess("注册成功");
	}
	
	@RequestMapping(value="/cashListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Example e = new Example(Cash.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<Cash> data = service.getListByExample(e);
		map.put("data", data);
		map.put("user", curUser);
		
		return "cash";
	}

	@RequestMapping("/checkcash")
	@ResponseBody
	public OutpubResult checkCash(Long cashId, HttpServletRequest request) {
		
		boolean admin = UserHelper.isAdmin(request);
		if (!admin) {
			return OutpubResult.getError("权限不足");
		}
		
		Cash c = service.getOne(cashId);
		c.setState(1);
		service.updateByPrimaryKeySelective(c);
		return OutpubResult.getSuccess("审核成功");
	}
	
}
