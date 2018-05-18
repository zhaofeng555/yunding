package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.IncreaseInfoMapper;
import com.haojg.model.IncreaseInfo;

import mybatis.customer.CustomerMapper;

@Service
public class IncreaseInfoService extends CustomService<IncreaseInfo> {

	@Autowired
	IncreaseInfoMapper mapper;
	
	@Override
	public CustomerMapper<IncreaseInfo> getMapper() {
		return mapper;
	}
	
}
