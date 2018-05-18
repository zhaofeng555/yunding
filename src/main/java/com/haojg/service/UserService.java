package com.haojg.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haojg.mapper.UserMapper;
import com.haojg.model.User;

import mybatis.customer.CustomerMapper;
import tk.mybatis.mapper.entity.Example;

@Service
public class UserService extends CustomService<User> {

	@Autowired
	UserMapper mapper;
	
	@Override
	public CustomerMapper<User> getMapper() {
		return mapper;
	}
	
	public User login(String username, String password) {
		Example e = new Example(User.class);
		e.createCriteria().andEqualTo("username", username)
		.andEqualTo("password", password);
		
		List<User> userList = getListByExample(e);
		if(CollectionUtils.isEmpty(userList)) {
			return null;
		}
		return userList.get(0);
	}
}
