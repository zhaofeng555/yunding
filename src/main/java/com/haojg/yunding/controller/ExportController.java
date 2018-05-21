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
import com.haojg.model.Export;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
import com.haojg.service.ExportService;
import com.haojg.service.CustomService;
import com.haojg.service.UserService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController<Export> {

	@Autowired
	ExportService service;
	
	@Autowired
	UserService userService;
	
	@Override
	public CustomService<Export> getService() {
		return service;
	}
	
	@RequestMapping(value="/apply", method=RequestMethod.POST)
	@ResponseBody
	public OutpubResult register(Export export, HttpServletRequest request){
		
		User curUser = UserHelper.getCurrentUser(request);
		
		curUser=userService.getOne(curUser.getId());
		
		Date startDateTime = curUser.getCreateTime();
		
		Date endDateTime = DateUtils.addDays(startDateTime, 180);
		
		Date curDateTime = new Date(System.currentTimeMillis());
		
		if(curDateTime.before(endDateTime)){
			return OutpubResult.getError("要过180天周期");
		}
		
		Double assets = curUser.getAssets();
		
		Integer num = export.getNum();
		if(assets<num){
			return OutpubResult.getSuccess("资产不足");
		}
		
		User toUser = userService.findByUsername(export.getToUsername());
		if(toUser == null){
			return OutpubResult.getSuccess("转出的用户不存在");
		}
		
		export.setCreateTime(new Date());
		export.setUpdateTime(new Date());
		export.setUserId(curUser.getId());
		export.setToUserId(toUser.getId());
		export.setState(0);
		
		service.insertSelective(export);
		
		curUser.setAssets(assets-num);
		userService.updateByPrimaryKeySelective(curUser);
		
		return OutpubResult.getSuccess("转出成功");
	}
	
	@RequestMapping(value="/exportListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Example e = new Example(Export.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<Export> data = service.getListByExample(e);
		map.put("data", data);
		map.put("user", curUser);
		
		return "export";
	}
}
