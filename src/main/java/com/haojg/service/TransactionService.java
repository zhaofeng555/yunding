package com.haojg.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haojg.constant.StateConstant;
import com.haojg.mapper.TransactionMapper;
import com.haojg.mapper.UserMapper;
import com.haojg.model.Transaction;
import com.haojg.model.User;
import com.haojg.output.OutpubResult;

import mybatis.customer.CustomerMapper;
import tk.mybatis.mapper.entity.Example;

@Service
public class TransactionService extends CustomService<Transaction> {

	@Autowired
	TransactionMapper mapper;

	@Override
	public CustomerMapper<Transaction> getMapper() {
		return mapper;
	}

	public void refreshTrasaction() {
		Example e = new Example(Transaction.class);
		// e.createCriteria().andGreaterThanOrEqualTo("createTime", value);
		getListByExample(e);
	}

	@Autowired
	UserService userService;

	@Autowired
	UserMapper userMapper;

	//验证
	@Transactional
	public OutpubResult verifyTransaction(User curUser, Long tranId) {
		Transaction c = getOne(tranId);

		// 只能确认自己的挂单
		if (!c.getUserId().equals(curUser.getId())) {
			return OutpubResult.getError("权限不足");
		}
		// 确认有人申购的挂单
		if (c.getState() != StateConstant.TANSACTION_WANT_BUY_STATE) {
			return OutpubResult.getError("状态异常");
		}

		c.setState(StateConstant.TANSACTION_COMPLETE_STATE);
		int ct = mapper.updateByPrimaryKeySelective(c);
		if (ct < 1) {
			throw new RuntimeException("更新Transaction失败");
		}

		// 给toUser转账
		Integer num = c.getNum();
		Double price = c.getPrice();
		Long toUserId = c.getToUserId();

		User toUser = userService.getOne(toUserId);
		if (toUser == null) {
			throw new RuntimeException("toUser用户不存在");
		}

		Double newAssets = price * num;

		Double assets = toUser.getAssets();

		User updateUser = new User();
		updateUser.setId(toUserId);
		updateUser.setAssets(assets + newAssets);
		ct = userMapper.updateByPrimaryKeySelective(updateUser);
		if (ct < 1) {
			throw new RuntimeException("更新 user 信息失败");
		}

		return OutpubResult.getSuccess("确认成功");
	}

	//申请
	@Transactional
	public OutpubResult applyTransaction(User curUser, Transaction tran) {

		curUser = userService.getOne(curUser.getId());
		Double assets = curUser.getAssets();

		Integer num = tran.getNum();
		Double cost = tran.getPrice() * num;

		if (assets < cost) {
			return OutpubResult.getError("资产不足");
		}

		tran.setCreateTime(new Date());
		tran.setUpdateTime(new Date());
		tran.setUserId(curUser.getId());
		tran.setState(StateConstant.TANSACTION_APPLY_STATE);

		int ct = mapper.insertSelective(tran);
		if (ct < 1) {
			throw new RuntimeException("插入Transaction失败");
		}

		Double surplusAssets = assets - cost;
		curUser.setAssets(surplusAssets);
		ct = userMapper.updateByPrimaryKeySelective(curUser);
		if (ct < 1) {
			throw new RuntimeException("更新 user 信息 失败");
		}

		return OutpubResult.getSuccess("注册成功");

	}
}
