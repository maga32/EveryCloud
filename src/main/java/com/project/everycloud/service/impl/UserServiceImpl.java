package com.project.everycloud.service.impl;

import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.UserMapper;
import com.project.everycloud.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Lazy
	UserUtil userUtil;
	
	@Override
	public UserDTO getUser(String id) {
		return userMapper.getUser(id);
	}

	@Override
	public String getUserPass(String id) {
		return userMapper.getUserPass(id);
	}

	@Override
	public UserDTO getAdmin() {
		return userMapper.getAdmin();
	}

	@Override
	public String checkUser(String id, String pass) {
		String result = "";
		
		if(userUtil.isUser(id, pass)) {
			result = "ok";
		} else {
			result = "id와 비밀번호를 확인해주세요.";
		}
		
		return result;
	}

	@Override
	public void updateUser(UserDTO user, String userOrigId) {
		if(user.getPass() != null && !user.getPass().equals("")) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
			user.setPass(pass.encode(user.getPass()));
		}
		userMapper.updateUser(user, userOrigId);
	}

}
