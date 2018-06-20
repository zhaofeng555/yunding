package com.haojg.yunding.controller;

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

import com.haojg.constant.StateConstant;
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

		return service.applyTransaction(curUser, tran);
	}
	
	@RequestMapping(value="/cancleApply", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult cancleApply(Long id, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		
		return service.cancleApplyTransaction(curUser, id);
	}
	
	@RequestMapping(value="/wantBuy", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Long id, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Transaction curTrans = service.getOne(id);
		if(curTrans.getState()!=StateConstant.TANSACTION_APPLY_STATE) {
			return OutpubResult.getError("已经有人申购");
		}
		
		Transaction c = new Transaction();
		
		//想要购买
		c.setId(id);
		c.setState(StateConstant.TANSACTION_WANT_BUY_STATE);
		c.setToUserId(curUser.getId());
		service.updateByPrimaryKeySelective(c);
		
		return OutpubResult.getSuccess("申购成功");
	}

	//申购人 撤销 想购买
	@RequestMapping(value="/cancleWantBuy", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult cancle(Long id, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		//撤销
		Transaction c = service.getOne(id);
		
		if(!c.getToUserId().equals(curUser.getId())) {
			return OutpubResult.getError("权限不足");
		}
		if(c.getState()!=StateConstant.TANSACTION_WANT_BUY_STATE) {
			return OutpubResult.getError("状态异常");
		}
		
		//只能撤销自己申请的
		if(c.getToUserId().equals(curUser.getId())){
			
			c.setState(StateConstant.TANSACTION_APPLY_STATE);
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
		
		return service.verifyTransaction(curUser, id);
	}
	
	@RequestMapping(value="/transactionListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		map.put("user", curUser);
		
		//my transaction list
		{
			Example myExample = new Example(Transaction.class);
			
			Set<Integer> stateSet = new HashSet<>();
			stateSet.add(StateConstant.TANSACTION_APPLY_STATE);
			stateSet.add(StateConstant.TANSACTION_WANT_BUY_STATE);
			myExample.createCriteria()
			.andEqualTo("userId", curUser.getId())
			.andIn("state", stateSet);
			
			List<Transaction> myData = service.getListByExample(myExample);
			map.put("myData", myData);
		}
		
		//system all transaction list
		{
			Example allExample = new Example(Transaction.class);
			Set<Integer> stateSet = new HashSet<>();
			stateSet.add(StateConstant.TANSACTION_APPLY_STATE);
			stateSet.add(StateConstant.TANSACTION_WANT_BUY_STATE);
			allExample.createCriteria().andIn("state", stateSet);
			
			List<Transaction> dataList = service.getListByExample(allExample);
			map.put("dataList", dataList);
		}
		
		//i want buy list
		{
			Example wantBuyExample = new Example(Transaction.class);
			Set<Integer> stateSet = new HashSet<>();
			stateSet.add(StateConstant.TANSACTION_WANT_BUY_STATE);
//			stateSet.add(2);
			wantBuyExample.createCriteria().andIn("state", stateSet)
			.andEqualTo("toUserId", curUser.getId());
			
			List<Transaction> dataList = service.getListByExample(wantBuyExample);
			map.put("wantBuyDataList", dataList);
		}
		
		return "transaction";
	}
	
	@RequestMapping(value="/viewSellUser", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult viewSellUser(Long userId, HttpServletRequest request){
		User u = userService.getOne(userId);
		
		User su = new User();
		su.setMobile(u.getMobile());
		su.setBankName(u.getBankName());
		su.setBankNo(u.getBankNo());
		su.setUsername(u.getUsername());
		su.setRealname(u.getRealname());
		su.setMail(u.getMail());
		
		return OutpubResult.getSuccess(su);
		
	}

	
}
