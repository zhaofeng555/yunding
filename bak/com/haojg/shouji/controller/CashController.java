package com.haojg.shouji.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.haojg.shouji.bean.Cash;
import com.haojg.shouji.bean.User;
import com.haojg.shouji.dao.CashDao;
import com.haojg.shouji.dao.UserDao;
import com.haojg.shouji.service.CashService;
import com.haojg.shouji.util.PageBuilder;
import com.haojg.shouji.util.UserHelper;

@Controller
@RequestMapping("/cash")
public class CashController {


	@Autowired
	UserDao userDao;
	@Autowired
	CashDao cashDao;
	@Autowired
	CashService cashService;
	
	@RequestMapping("/listing")
	public String listing(Model m, HttpServletRequest request) {

		Pageable pageable = PageBuilder.generate(request);
		int status = 1;
		findByStatus(m, status, pageable);
		return "cash/listing";
	
	}
	
	@RequestMapping("/activing")
	public String activing(Model m, HttpServletRequest request) {
		Pageable pageable = PageBuilder.generate(request);
		int status = 0;
		findByStatus(m, status, pageable);
		return "cash/activing";
	}
	
	
	public void findByStatus(Model m, int status, Pageable pageable) {
		Cash cach = new Cash();
		cach.setStatus(status);
		cashService.findAllPro(m, pageable, cach);
	}

	@RequestMapping("/checkcash")
	@ResponseBody
	public Map<String, Object> checkCash(Long cashId, HttpSession session) {
		Map<String, Object> rs = Maps.newHashMap();
		
		boolean admin = UserHelper.isAdmin(session);
		if (!admin) {
			rs.put("code", 1);
			rs.put("msg", "权限不足");
			return rs;
		}
		
		Cash c = cashDao.findOne(cashId);
		c.setStatus(1);
		cashDao.save(c);
		rs.put("code", 0);
		return rs;
	}

	@RequestMapping("/createcash")
	@ResponseBody
	public Map<String, Object> createCash(Integer num, HttpServletRequest request) {

		Map<String, Object> rs = Maps.newHashMap();
		User curUser = UserHelper.getCurrentUser(request);
		User user = userDao.findOne(curUser.getId());

		if (user.getMoneyNum() < num) {
			rs.put("code", 1);
			rs.put("msg", "提现金额不足");
			return rs;
		}

		user.setMoneyNum(user.getMoneyNum() - num);
		userDao.save(user);

		Cash c = new Cash();
		c.setStatus(0);
		c.setCreateTime(new Date());
		c.setNum(num);
		c.setUserId(user.getId());
		c.setUsername(user.getUsername());
		c.setRealName(user.getRealName());
		c.setBankName(user.getBankName());
		c.setBankNo(user.getBankNo());
		c.setTelephoneNo(user.getTelephoneNo());
		cashDao.save(c);

		rs.put("code", 0);
		return rs;
	}

	
}
