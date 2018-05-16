package com.haojg.shouji.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.haojg.shouji.bean.Cash;
import com.haojg.shouji.bean.User;
import com.haojg.shouji.dao.CashDao;
import com.haojg.shouji.dao.NoticeDao;
import com.haojg.shouji.dao.UserDao;
import com.haojg.shouji.form.CashForm;
import com.haojg.shouji.form.NoticeForm;
import com.haojg.shouji.form.UserForm;
import com.haojg.shouji.service.CashService;
import com.haojg.shouji.service.NoticeService;
import com.haojg.shouji.service.UserService;
import com.haojg.shouji.util.PageBuilder;
import com.haojg.shouji.util.UserHelper;
import com.haojg.shouji.util.WebMiscMethod;

@RequestMapping("/user")
@Controller
public class UserControlelr {
	private static Logger logger = LoggerFactory.getLogger(UserControlelr.class);
	
	@Autowired
	UserDao userDao;

	@RequestMapping("/listing")
	public String listing(Model m, HttpServletRequest request) {
		Pageable pageable = PageBuilder.generate(request);

		Page<User> users = userDao.findAll(pageable);
		m.addAttribute("users", users);
		System.out.println(users);
		return "user/list";
	}

	@RequestMapping("/activing")
	public String activing(Model m, HttpServletRequest request) {
		
		Pageable pageable = PageBuilder.generate(request);
		User u = new User();
		u.setStatus(0);
		Example<User> e = Example.of(u);

		Page<User> users = userDao.findAll(e, pageable);
		m.addAttribute("users", users);
		System.out.println(users);
		return "user/activing";
	}

	@PostMapping("/login")
	public String login(Model m, String username, String password, HttpSession session) {
		User user = userDao.findByUsernameAndPassword(username, password);
		session.setAttribute("user", user);
		if (user == null) {
			return "redirect:/login?error=1";
		} else {
			if (user.getRole() != null && user.getRole() == 0) {
				return "redirect:/user/activing";
			} else {
				m.addAttribute("user", user);
				return "redirect:/user/info";
			}
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("user");
		return "redirect:/login";
	}

	@RequestMapping("/active")
	@ResponseBody
	public Map<String, Object> activeUser(Long userId, HttpSession session) {

		Map<String, Object> rs = Maps.newHashMap();
		rs.put("code", 0);

		boolean admin = UserHelper.isAdmin(session);
		if (!admin) {
			rs.put("code", 1);
			rs.put("msg", "权限不足");
			return rs;
		}
		boolean isSuccess = userService.activeUser(userId);
		if (isSuccess) {
			return rs;
		}

		rs.put("code", 2);
		rs.put("msg", "激活失败");

		return rs;
	}

	@Autowired
	UserService userService;

	@RequestMapping("/register")
	public String register(User user) {
		user.setRole(1);
		user.setStatus(0);
		user.setMoneyNum(0);
		user.setCreateTime(new Date());
		user.setUpdateTime(new Date());
		User u = userDao.save(user);
		if (u.getId() != null) {
			return "redirect:/result";
		}
		return "redirect:/result_error";
	}

	@PostMapping("/get")
	@ResponseBody
	public Map<String, Object> getUser(Long id, HttpSession session) {
		Map<String, Object> rs = Maps.newHashMap();
		User user = userDao.findOne(id);
		UserForm uf = new UserForm();
		BeanUtils.copyProperties(user, uf);
		rs.put("user", uf);
		return rs;
	}
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> delUser(Long userId, HttpSession session) {
		Map<String, Object> rs = Maps.newHashMap();
		boolean isAdmin = UserHelper.isAdmin(session);
		if(!isAdmin) {
			rs.put("code", 1);
			return rs;
		}
		userDao.delete(userId);
		rs.put("code", 0);
		return rs;
	}

	@RequestMapping("save")
	@ResponseBody
	public Map<String, Object> updateUser(User user) {
		Map<String, Object> rs = Maps.newHashMap();
		user.setUpdateTime(new Date());
		userDao.save(user);
		rs.put("code", 0);
		return rs;
	}

	@Autowired
	CashDao cashDao;
	@Autowired
	CashService cashService;
	
	@RequestMapping("/info")
	public String userInfo(Model m, HttpSession session) {
		User u = (User) session.getAttribute("user");

		u = userDao.findOne(u.getId());

		UserForm uf = new UserForm();
		BeanUtils.copyProperties(u, uf);
		if (u.getRecUserId() != null) {
			User recUser = userDao.findOne(u.getRecUserId());
			uf.setRecUsername(recUser.getUsername());
		} else {
			uf.setRecUsername("-");
		}
		m.addAttribute("user", uf);

		Long recUserId = u.getId();

		// first
		List<User> firstRecUserList = userDao.findByRecUserIdIn(new Long[] { recUserId });
		m.addAttribute("first", firstRecUserList);
		List<Long> firstUserIdList = Lists.newArrayList();
		for (User recUser : firstRecUserList) {
			firstUserIdList.add(recUser.getId());
		}

		// second
		List<User> secondRecUserList = userDao.findByRecUserIdIn(firstUserIdList.toArray(new Long[0]));
		m.addAttribute("second", secondRecUserList);

		List<Long> secondRecUserIdList = Lists.newArrayList();
		for (User user : secondRecUserList) {
			secondRecUserIdList.add(user.getId());
		}

		// third
		List<User> thirdRecUserList = userDao.findByRecUserIdIn(secondRecUserIdList.toArray(new Long[0]));
		m.addAttribute("third", thirdRecUserList);
		List<Long> thirdRecUserIdList = Lists.newArrayList();
		for (User user : thirdRecUserList) {
			thirdRecUserIdList.add(user.getId());
		}
		

		List<Long> othersLevelId=Lists.newArrayList();
		
		Integer teamSum = 1;
		
		othersLevelId.addAll(thirdRecUserIdList);
		while(!othersLevelId.isEmpty()) {
			List<User> otherUsers = userDao.findByRecUserIdIn(othersLevelId.toArray(new Long[0]));
			othersLevelId.clear();
			
			if(otherUsers==null || otherUsers.isEmpty()) {
				break;
			}
			
			teamSum += otherUsers.size();
			
			for(User otherUser : otherUsers) {
				othersLevelId.add(otherUser.getId());
			}
		}
		
		 teamSum = teamSum + firstRecUserList.size() + secondRecUserList.size() + thirdRecUserList.size();
		m.addAttribute("teamSum", teamSum);

		Page<NoticeForm> notices = noticeService.listNotices(PageBuilder.generate(0, 5));
		m.addAttribute("notices", notices);
		
		cashService.findByUserId(m, u.getId(), PageBuilder.generate(0, 20));
		
		return "user/info";
	}

	@RequestMapping("rec_user")
	public String recUserList(Model m, Integer level, HttpServletRequest request) {
		User u = (User) UserHelper.getCurrentUser(request);
		Pageable pageable = PageBuilder.generate(request);

		// first
		List<User> firstRecUserList = userDao.findByRecUserIdIn(new Long[] { u.getId() });
		m.addAttribute("first", firstRecUserList);
		List<Long> firstUserIdList = Lists.newArrayList();
		for (User recUser : firstRecUserList) {
			firstUserIdList.add(recUser.getId());
		}

		if (level == 1) {
			m.addAttribute("title", "第一代推荐");
			m.addAttribute("users", firstRecUserList);
			return "rec_user_list_1";
		}

		// second
		List<User> secondRecUserList = userDao.findByRecUserIdIn(firstUserIdList.toArray(new Long[0]));
		m.addAttribute("second", secondRecUserList);

		if (level == 2) {
			m.addAttribute("title", "第二代推荐");
			m.addAttribute("users", secondRecUserList);
			return "rec_user_list_1";
		}
		
		List<Long> secondRecUserIdList = Lists.newArrayList();
		for (User user : secondRecUserList) {
			secondRecUserIdList.add(user.getId());
		}

		// third
		List<User> thirdRecUserList = userDao.findByRecUserIdIn(secondRecUserIdList.toArray(new Long[0]));
		m.addAttribute("third", thirdRecUserList);

		if (level == 3) {
			m.addAttribute("title", "第三代推荐");
			m.addAttribute("users", thirdRecUserList);
			return "rec_user_list_1";
		}
		
		return "rec_user_list_1";

	}

	@PostMapping("/changePasswd")
	@ResponseBody
	public Map<String, Object> changePasswd(String oldPassword, String newPassword, HttpServletRequest request) {

		Map<String, Object> rs = Maps.newHashMap();
		User currentUser = UserHelper.getCurrentUser(request);
		User user = userDao.findOne(currentUser.getId());
		if (!user.getPassword().equals(oldPassword)) {
			rs.put("code", 1);
			rs.put("msg", "原始密码错误");
			return rs;
		}
		if (StringUtils.isEmpty(newPassword)) {
			rs.put("code", 2);
			rs.put("msg", "新密码不能为空");
			return rs;
		}
		user.setPassword(newPassword);
		userDao.save(user);

		rs.put("code", 0);
		return rs;

	}

	@Autowired
	NoticeService noticeService;

	@Autowired
	NoticeDao noticeDao;

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		binder.registerCustomEditor(Date.class, dateEditor);
	}

}
