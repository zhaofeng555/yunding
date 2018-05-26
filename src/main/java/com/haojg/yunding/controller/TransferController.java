package com.haojg.yunding.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.haojg.controller.BaseController;
import com.haojg.model.Transfer;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
import com.haojg.service.TransferService;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/transfer")
public class TransferController extends BaseController<Transfer> {

	@Autowired
	TransferService service;
	
	@Autowired
	UserService userService;
	
	@Override
	public CustomService<Transfer> getService() {
		return service;
	}
	
	@RequestMapping(value="/apply", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Transfer tran, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		
		curUser=userService.getOne(curUser.getId());
		

		Date startDateTime = curUser.getCreateTime();
		
		Date endDateTime = DateUtils.addDays(startDateTime, 180);
		
		Date curDateTime = new Date(System.currentTimeMillis());
		
		if(curDateTime.before(endDateTime)){
			return OutpubResult.getError("要过180天周期");
		}
		
		Integer perGu = 2000;
		
		Double assets = curUser.getAssets();
		Integer stockNum = curUser.getStockNum();
		
		Integer num = tran.getNum();
		if(assets<num || num < perGu){
			return OutpubResult.getSuccess("资产不足");
		}
		
		Transfer c = new Transfer();
		c.setCreateTime(new Date());
		c.setUpdateTime(new Date());
		c.setUserId(curUser.getId());
		c.setNum(num);
		c.setState(1);
		
		service.insertSelective(c);
		
		int addStockNum = num/perGu;
		
		//减少浮动资产
		curUser.setAssets(assets-(addStockNum*num));
		
		//增加股份资产
		curUser.setStockNum(stockNum+addStockNum);
		
		userService.updateByPrimaryKeySelective(curUser);
		
		return OutpubResult.getSuccess("转换成功");
	}
	
	
	@RequestMapping(value="/transferListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		curUser = userService.getOne(curUser.getId());
		Example e = new Example(Transfer.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<Transfer> data = service.getListByExample(e);
		map.put("data", data);
		map.put("user", curUser);
		
		return "transfer";
	}

	
}
