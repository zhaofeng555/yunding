package com.haojg.shouji.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.shouji.bean.Commission;
import com.haojg.shouji.bean.User;
import com.haojg.shouji.dao.CommissionDao;
import com.haojg.shouji.dao.UserDao;
@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	CommissionDao commissionDao;
	
	public boolean activeUser(Long userId) {
		
		User curUser = userDao.findOne(userId);
		curUser.setStatus(1);
		curUser.setUpdateTime(new Date());
		curUser.setActiveTime(new Date());
		userDao.save(curUser);
		
		
		Integer totalMoney = curUser.getMobileTotalMoney();
		
		//一级推荐人
		Long recUserId = curUser.getRecUserId();
		if(recUserId == null) {
			return true;
		}
		
		User recUser_1 = userDao.findOne(recUserId);
		if(recUser_1==null) {
			return true;
		}
		
		//一代5% 提成
		Integer firstLevelMoneyNum = Double.valueOf(totalMoney*0.05).intValue();
		recUser_1.setMoneyNum(recUser_1.getMoneyNum()+firstLevelMoneyNum);
		userDao.saveAndFlush(recUser_1);
		
		//save first commission
		Commission first = new Commission();
		first.setActiveUserId(userId);
		first.setRecUserId(recUserId);
		first.setCreateTime(new Date());
		first.setNum(firstLevelMoneyNum);
		first.setRemark("激活 "+userId+", 第一代推荐人 "+recUserId+" 提成  "+firstLevelMoneyNum);
		commissionDao.saveAndFlush(first);
		
		if(recUser_1.getRecUserId() == null) {
			return true;
		}
		
		//二代 不提成
		User recUser_2 = userDao.findOne(recUser_1.getRecUserId());
		if(recUser_2==null) {
			return true;
		}
		if(recUser_2.getRecUserId() == null) {
			return true;
		}
		
		//三代提成 3%，
		User recUser_3 = userDao.findOne(recUser_2.getRecUserId());
		if(recUser_3==null) {
			return true;
		}
		
		
		int thirdLevelMoneyNum = Double.valueOf(totalMoney*0.03).intValue();
		recUser_3.setMoneyNum(recUser_3.getMoneyNum()+thirdLevelMoneyNum);
		userDao.saveAndFlush(recUser_3);
		
		//save third commission
		Commission third = new Commission();
		third.setActiveUserId(userId);
		third.setRecUserId(recUser_2.getRecUserId());
		third.setCreateTime(new Date());
		third.setNum(thirdLevelMoneyNum);
		third.setRemark("激活 "+userId+", 第三代推荐人 "+recUser_2.getRecUserId()+" 提成  "+thirdLevelMoneyNum);
		commissionDao.saveAndFlush(third);
		
		return true;
	}

}


