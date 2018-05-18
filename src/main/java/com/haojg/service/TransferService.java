package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.TransferMapper;
import com.haojg.model.Transfer;

import mybatis.customer.CustomerMapper;

@Service
public class TransferService extends CustomService<Transfer> {

	@Autowired
	TransferMapper mapper;
	
	@Override
	public CustomerMapper<Transfer> getMapper() {
		return mapper;
	}
	
}
