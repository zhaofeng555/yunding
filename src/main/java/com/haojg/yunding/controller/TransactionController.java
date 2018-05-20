package com.haojg.yunding.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import com.haojg.service.CustomService;
import com.haojg.service.TransactionService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/transaction")
public class TransactionController extends BaseController<Transaction> {

	//state: 0：申请，1：有人想购买，2：确认交易
	
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
			return OutpubResult.getError("资产不足");
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
	
	@RequestMapping(value="/wantBuy", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Long id, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Transaction c = new Transaction();
		
		//想要购买
		c.setId(id);
		c.setState(1);
		c.setToUserId(curUser.getId());
		service.updateByPrimaryKeySelective(c);
		
		return OutpubResult.getSuccess("申购成功");
	}

	//申购人 撤销 想购买
	@RequestMapping(value="/cancle", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult cancle(Long id, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		
		//撤销
		Transaction c = service.getOne(id);
		
		//只能撤销自己申请的
		if(c.getToUserId().equals(curUser.getId())){
			
			c.setState(0);
			c.setToUserId(0L);
			service.updateByPrimaryKeySelective(c);
		}else{
			return OutpubResult.getError("撤销失败");
		}
		
		return OutpubResult.getSuccess("申购成功");
	}
	
	@RequestMapping(value="/verify", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult verify(Long id, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		
		//撤销
		Transaction c = service.getOne(id);
		
		//只能确认自己的挂单
		if(c.getUserId().equals(curUser.getId())){
			
			//确认有人申购的挂单
			if(c.getState() == 1){
				c.setState(2);
				service.updateByPrimaryKeySelective(c);
				
				
				//给toUser转账
				Integer num = c.getNum();
				Long toUserId = c.getToUserId();
				
				User toUser = userService.getOne(toUserId);
				if(toUser == null){
					throw new RuntimeException("toUser用户不存在");
				}
				
				Double assets = toUser.getAssets();
				
				User updateUser = new User();
				updateUser.setId(toUserId);
				updateUser.setAssets(assets+num);
				userService.updateByPrimaryKeySelective(updateUser);
				
			}else{
				return OutpubResult.getError("挂单状态不对");
			}
			
		}else{
			return OutpubResult.getError("权限不足");
		}
		
		return OutpubResult.getSuccess("确认成功");
	}
	
	@RequestMapping(value="/transactionListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		map.put("user", curUser);
		
		//my transaction list
		{
			Example myExample = new Example(Transaction.class);
			myExample.createCriteria().andEqualTo("userId", curUser.getId());
			
			List<Transaction> myData = service.getListByExample(myExample);
			map.put("myData", myData);
		}
		
		//system all transaction list
		{
			Example allExample = new Example(Transaction.class);
			Set<Integer> stateSet = new HashSet<>();
			stateSet.add(0);
			stateSet.add(1);
			allExample.createCriteria().andIn("state", stateSet);
			
			List<Transaction> dataList = service.getListByExample(allExample);
			map.put("dataList", dataList);
		}
		
		//i want buy list
		{
			Example wantBuyExample = new Example(Transaction.class);
			Set<Integer> stateSet = new HashSet<>();
			stateSet.add(1);
			stateSet.add(2);
			wantBuyExample.createCriteria().andIn("state", stateSet)
			.andEqualTo("toUserId", curUser.getId());
			
			List<Transaction> dataList = service.getListByExample(wantBuyExample);
			map.put("wantBuyDataList", dataList);
		}
		
		return "transaction";
	}

	
}
