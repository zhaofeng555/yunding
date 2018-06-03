package com.haojg.service;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haojg.mapper.ExportMapper;
import com.haojg.model.Export;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;

import mybatis.customer.CustomerMapper;

@Service
public class ExportService extends CustomService<Export> {

	@Autowired
	ExportMapper mapper;
	
	@Override
	public CustomerMapper<Export> getMapper() {
		return mapper;
	}
	

	@Autowired
	UserService userService;
	
	@Transactional
	public OutpubResult exportToUser(Export export, User curUser) {
		curUser = userService.getOne(curUser.getId());

		// 提现浮动资产
		Integer cashAssets = export.getAssets();
		// 提现固定资产
		Integer cashBuyNum = export.getBuyNum();

		Export c = new Export();
		User updateCurUser = new User();
		updateCurUser.setId(curUser.getId());
		
		User toUser = userService.findByUsername(export.getToUsername());
		if (toUser == null) {
			return OutpubResult.getSuccess("转出的用户不存在");
		}
		User updateToUser = new User();
		updateToUser.setId(toUser.getId());
		

		if (cashBuyNum != null && cashBuyNum > 0) {

			Date startDateTime = curUser.getCreateTime();

			Date endDateTime = DateUtils.addDays(startDateTime, 180);

			Date curDateTime = new Date(System.currentTimeMillis());

			if (curDateTime.before(endDateTime)) {
				return OutpubResult.getError("要过180天周期");
			}

			Double curBuyNum = curUser.getBuyNum();
			if (curBuyNum < cashBuyNum) {
				return OutpubResult.getError("固定资产不足");
			}

			c.setBuyNum(cashBuyNum);
			c.setAssets(0);
			updateCurUser.setBuyNum(curBuyNum - cashBuyNum);
			
			Double touUserBuyNum = toUser.getBuyNum();
			updateToUser.setBuyNum(touUserBuyNum+cashBuyNum);

		} else if (cashAssets != null && cashAssets > 0) {

			Double curAssets = curUser.getAssets();
			if (curAssets < cashAssets) {
				return OutpubResult.getError("浮动资产不足");
			}

			c.setAssets(cashAssets);
			c.setBuyNum(0);
			updateCurUser.setAssets(curAssets - cashAssets);
			
			Double toUserAssets = toUser.getAssets();
			updateToUser.setAssets(toUserAssets + cashAssets);
			
			
		} else {
			return OutpubResult.getError("申请失败");
		}

		c.setCreateTime(new Date());
		c.setUpdateTime(new Date());
		c.setUserId(curUser.getId());
		c.setToUserId(toUser.getId());
		c.setToUsername(toUser.getUsername());
		c.setState(0);

		int ct = this.insertSelective(c);
		if (ct > 0) {
			userService.updateByPrimaryKeySelective(updateCurUser);
			userService.updateByPrimaryKeySelective(updateToUser);
		}

		return OutpubResult.getSuccess("转出成功");
	}
}
