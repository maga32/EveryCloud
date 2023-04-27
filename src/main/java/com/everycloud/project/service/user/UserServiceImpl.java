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
	public User getUser(String id) {
		return userDao.getUser(id);
	}

	@Override
	public String getUserPass(String id) {
		return userDao.getUserPass(id);
	}

	@Override
	public User getAdmin() {
		return userDao.getAdmin();
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
	public void updateUser(User user, String userOrigId) {
		if(user.getPass() != null && !user.getPass().equals("")) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
			user.setPass(pass.encode(user.getPass()));
		}
		userDao.updateUser(user, userOrigId);
	}

}
