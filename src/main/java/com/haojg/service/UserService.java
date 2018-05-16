package com.haojg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.UserMapper;
import com.haojg.model.User;

import mybatis.customer.CustomerMapper;

@Service
public class UserService extends CustomService<User> {

	@Autowired
	UserMapper mapper;
	
	@Override
	public CustomerMapper<User> getMapper() {
		return mapper;
	}
	
}
