package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.NoticeMapper;
import com.haojg.model.Notice;

import mybatis.customer.CustomerMapper;

@Service
public class NoticeService extends CustomService<Notice> {

	@Autowired
	NoticeMapper mapper;
	
	@Override
	public CustomerMapper<Notice> getMapper() {
		return mapper;
	}
	
}
