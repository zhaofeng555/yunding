package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.TransactionInfoMapper;
import com.haojg.model.TransactionInfo;

import mybatis.customer.CustomerMapper;

@Service
public class TransactionInfoService extends CustomService<TransactionInfo> {

	@Autowired
	TransactionInfoMapper mapper;
	
	@Override
	public CustomerMapper<TransactionInfo> getMapper() {
		return mapper;
	}
	
}
