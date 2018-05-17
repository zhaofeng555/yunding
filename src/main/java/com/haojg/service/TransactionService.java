package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.TransactionMapper;
import com.haojg.model.Transaction;

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
	

	
	public void refreshTrasaction(){
		Example e = new Example(Transaction.class);
//		e.createCriteria().andGreaterThanOrEqualTo("createTime", value);
		getListByExample(e);
	}
	
}
