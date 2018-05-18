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
import com.haojg.model.IncreaseInfo;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;
import com.haojg.service.CustomService;
import com.haojg.service.IncreaseInfoService;
import com.haojg.util.UserHelper;

import tk.mybatis.mapper.entity.Example;

@Controller
@RequestMapping("/increaseInfo")
public class IncreaseInfoController extends BaseController<IncreaseInfo> {

	@Autowired
	IncreaseInfoService service;
	
	@Override
	public CustomService<IncreaseInfo> getService() {
		return service;
	}
	@RequestMapping(value="/increaseInfoListing", method=RequestMethod.GET)
	public String registerList(ModelMap map, HttpServletRequest request){
		User curUser = UserHelper.getCurrentUser(request);
		
		Example e = new Example(IncreaseInfo.class);
		e.createCriteria().andEqualTo("userId", curUser.getId());
		
		List<IncreaseInfo> data = service.getListByExample(e);
		map.put("data", data);
		
		return "increaseInfo";
	}

	
}
