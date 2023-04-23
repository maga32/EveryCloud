package com.everycloud.project.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.everycloud.project.dao.user.UserDao;
import com.everycloud.project.domain.User;
import com.everycloud.project.util.UserUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	UserUtil userUtil;
	
	@Override
	public User getUser(String userId) {
		return userDao.getUser(userId);
	}

	@Override
	public String getUserPass(String userId) {
		return userDao.getUserPass(userId);
	}

	@Override
	public User getAdmin() {
		return userDao.getAdmin();
	}

	@Override
	public String checkUser(String userId, String userPass) {
		String result = "";
		
		if(userUtil.isUser(userId, userPass)) {
			result = "ok";
		} else {
			result = "id와 비밀번호를 확인해주세요.";
		}
		
		return result;
	}

	@Override
	public void updateUser(User user, String userOrigId) {
		if(user.getUserPass() != null && !user.getUserPass().equals("")) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
			user.setUserPass(pass.encode(user.getUserPass()));
		}
		userDao.updateUser(user, userOrigId);
	}

}
