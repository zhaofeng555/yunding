package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.ExportMapper;
import com.haojg.model.Export;

import mybatis.customer.CustomerMapper;

@Service
public class ExportService extends CustomService<Export> {

	@Autowired
	ExportMapper mapper;
	
	@Override
	public CustomerMapper<Export> getMapper() {
		return mapper;
	}
	
}
