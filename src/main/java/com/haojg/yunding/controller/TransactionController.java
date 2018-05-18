package com.haojg.yunding.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojg.controller.BaseController;
import com.haojg.model.Transaction;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
import com.haojg.service.TransactionService;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/transaction")
public class TransactionController extends BaseController<Transaction> {

	@Autowired
	TransactionService service;
	
	@Autowired
	UserService userService;
	
	@Override
	public CustomService<Transaction> getService() {
		return service;
	}
	
	@RequestMapping(value="/apply", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Transaction tran, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		
		curUser=userService.getOne(curUser.getId());
		
		Double assets = curUser.getAssets();
		
		Integer num = tran.getNum();
		if(assets<num){
			return OutpubResult.getSuccess("资产不足");
		}
		
		Transaction c = new Transaction();
		c.setCreateTime(new Date());
		c.setUpdateTime(new Date());
		c.setUserId(curUser.getId());
		c.setNum(num);
		c.setPrice(tran.getPrice());
		c.setState(0);
		
		service.insertSelective(c);
		
		curUser.setAssets(assets-num);
		userService.updateByPrimaryKeySelective(curUser);
		
		return OutpubResult.getSuccess("注册成功");
	}
	
	@RequestMapping(value="/transactionListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Example e = new Example(Transaction.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<Transaction> data = service.getListByExample(e);
		map.put("data", data);
		map.put("user", curUser);
		
		return "transaction";
	}

	
}
