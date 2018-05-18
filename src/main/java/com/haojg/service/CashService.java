package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.CashMapper;
import com.haojg.model.Cash;

import mybatis.customer.CustomerMapper;

@Service
public class CashService extends CustomService<Cash> {

	@Autowired
	CashMapper mapper;
	
	@Override
	public CustomerMapper<Cash> getMapper() {
		return mapper;
	}
	
}
