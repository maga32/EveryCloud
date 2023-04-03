package com.everycloud.project.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everycloud.project.dao.user.UserDao;
import com.everycloud.project.domain.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;
	
	@Override
	public User getUser(String userId) {
		return userDao.getUser(userId);
	}

	@Override
	public String checkUser(String userId, String userPass) {
		String result = "";
		
		if(userPass.equals(userDao.getUserPass(userId))) {
			result = "ok";
		} else {
			result = "id와 비밀번호를 확인해주세요.";
		}
		
		return result;
	}

}
