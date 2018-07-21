package com.haojg.yunding.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haojg.controller.BaseController;
import com.haojg.model.Cash;
import com.haojg.model.User;
import com.haojg.output.CashForm;
import com.haojg.output.OutpubResult;
import com.haojg.service.CashService;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
	public OutpubResult register(Cash cash, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		curUser=userService.getOne(curUser.getId());
		
		//提现浮动资产
		Integer cashAssets = cash.getAssets();
		//提现固定资产
		Integer cashBuyNum = cash.getBuyNum();
		
		
		Cash c = new Cash();
		User updateUser = new User();
		updateUser.setId(curUser.getId());
		
		if(cashBuyNum!=null && cashBuyNum > 0){
			
			Date startDateTime = curUser.getCreateTime();
			
			Date endDateTime = DateUtils.addDays(startDateTime, 180);
			
			Date curDateTime = new Date(System.currentTimeMillis());
			
			if(curDateTime.before(endDateTime)){
				return OutpubResult.getError("要过180天周期");
			}
			
			if(cashBuyNum%500 != 0){
				return OutpubResult.getError("提现金额必须500的整数倍");
			}
			
			Double curBuyNum = curUser.getBuyNum();
			if(curBuyNum<cashBuyNum){
				return OutpubResult.getError("资产不足");
			}
			
			c.setBuyNum(cashBuyNum);
			c.setAssets(0);
			updateUser.setBuyNum(curBuyNum-cashBuyNum);
			
		}else if(cashAssets!=null && cashAssets>0){
			
			if(cashAssets%500 != 0){
				return OutpubResult.getError("提现金额必须500的整数倍");
			}
			
			Double curAssets = curUser.getAssets();
			if(curAssets<cashAssets){
				return OutpubResult.getError("资产不足");
			}
			
			c.setAssets(cashAssets);
			c.setBuyNum(0);
			updateUser.setAssets(curAssets-cashAssets);
		}else{
			return OutpubResult.getError("申请失败");
		}
		
		
		
		c.setCreateTime(new Date());
		c.setUpdateTime(new Date());
		c.setUserId(curUser.getId());
		c.setBankName(curUser.getBankName());
		c.setBankNo(curUser.getBankNo());
		c.setMobile(curUser.getMobile());
		c.setUsername(curUser.getUsername());
		c.setState(0);
		
		int ct = service.insertSelective(c);
		if(ct>0){
			userService.updateByPrimaryKeySelective(updateUser);
		}
		
		return OutpubResult.getSuccess("申请成功");
	}
	
	
	Long oneDay = 1000*60*60*24L;
	private Long getDays(Date endDateTime){
		Long day = 0L;
		Long curTime = System.currentTimeMillis();
		Long endTime=endDateTime.getTime();
		if(curTime < endTime){
			Long shengyuTime = endTime - curTime;
			
			day = shengyuTime/oneDay;
			
			if(shengyuTime%oneDay != 0){
				day += 1;
			}
			
		}
		return day;
	}
	
	@RequestMapping(value="/cashListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		curUser = userService.getOne(curUser.getId());
		Date startDateTime = curUser.getCreateTime();
		Date endDateTime = DateUtils.addDays(startDateTime, 180);
		Long days = getDays(endDateTime);
		
		
		Example e = new Example(Cash.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<Cash> data = service.getListByExample(e);
		map.put("data", data);
		map.put("user", curUser);
		map.put("days", days);
		
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

	@RequestMapping(value="/listing", method=RequestMethod.GET)
	public String list(
			@RequestParam(required=false, defaultValue="1")Integer pageNum,
			@RequestParam(required=false, defaultValue="15")Integer pageSize,
			ModelMap map) {

		String simpleClassName = entityClass.getSimpleName().toLowerCase();
		List<Cash> list = getService().getPageList(pageNum, pageSize);

		List<Long> userIdList = Lists.newArrayList();

		for(Cash c : list){
			userIdList.add(c.getUserId());
		}

		Example e =  new Example(User.class);
		e.createCriteria().andIn("id", userIdList);
		List<User> userList = userService.getListByExample(e);
		Map<Long, String> userIdUserNameMap = Maps.newHashMap();
		for (User user : userList) {
			userIdUserNameMap.put(user.getId(), user.getRealname());
		}

		List<CashForm> cashExtList = Lists.newArrayList();

		for (Cash cash : list) {
			CashForm cf = new CashForm();
			BeanUtils.copyProperties(cash, cf);
			cf.setRealname(userIdUserNameMap.get(cf.getUserId()));
			cashExtList.add(cf);
		}

		Integer sum = getService().count();
		Integer total = sum/pageSize;
		if(sum%pageSize != 0){
			++total;
		}

		map.put("data", cashExtList);
		map.put("pageNum", pageNum);
		map.put("total", total);
		map.put("isFirst", (pageNum<=1));
		map.put("isLast", (pageNum>=total));
		map.put("pageSize", pageSize);
		return simpleClassName+"/list";
	}


}
