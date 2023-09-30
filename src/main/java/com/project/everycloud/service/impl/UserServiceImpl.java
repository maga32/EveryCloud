package com.project.everycloud.service.impl;

import com.project.everycloud.common.exception.NeedAdminException;
import com.project.everycloud.common.exception.NeedLoginException;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Override
	public UserDTO getSessionUser(UserDTO user) {
		if(checkUserType(user) == 3) throw new NeedAdminException();
		return user;
	}

	@Override
	public UserDTO getUser(String id) {
		return userMapper.getUser(id);
	}

	@Override
	public String getUserPass(String id) {
		return userMapper.getUserPass(id);
	}

	/**
	 * return admin data
	 *
	 * @return UserDTO
	 */
	private UserDTO getAdmin() {
		return userMapper.getAdmin();
	}

	@Override
	public String checkUser(String id, String pass) {
		String result = "";

		if(isUser(id, pass)) {
			result = "ok";
		} else {
			result = "id와 비밀번호를 확인해주세요.";
		}

		return result;
	}

	@Override
	public UserDTO updateUserForm(HashMap<String, Object> paramMap) {
		UserDTO returnUser = new UserDTO();
		UserDTO user = (UserDTO) paramMap.get("user");
		String id = (String) paramMap.get("id");

		int userType = checkUserType(user);
		if(userType == 0) throw new NeedLoginException();

		if(!StringUtils.hasText(id) && isAdmin(user)) {
			returnUser = getAdmin();
			if(getUserPass(returnUser.getId()).equals("admin")) returnUser.setPass("admin");
		} else if(isAdmin(user) || user.getId().equals(id)) {
			returnUser = getUser(id);
		}

		return returnUser;
	}

	@Override
	public void updateUser(UserDTO user, String userOrigId) {
		if(StringUtils.hasText(user.getPass())) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
			user.setPass(pass.encode(user.getPass()));
		}
		userMapper.updateUser(user, userOrigId);
	}

	@Override
	public int checkUserType(UserDTO user) {
		int userType = 0;
		UserDTO admin = getAdmin();

		if(user != null) {
			if(user.getId().equals(admin.getId())) {
				userType = 2;
			} else {
				userType = 1;
			}
		} else if(getUserPass(admin.getId()).equals("admin")) {
			userType = 3;
		}

		return userType;
	}

	@Override
	public boolean isUser(UserDTO user) {
		int userType = checkUserType(user);
		if(userType != 0) return true;
		return false;
	}

	@Override
	public boolean isUser(String userId) {
		if(getUser(userId) != null) return true;
		return false;
	}

	@Override
	public boolean isUser(String userId, String userPass) {
		BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
		if(pass.matches(userPass, getUserPass(userId))) return true;
		return false;
	}

	@Override
	public boolean isAdmin(UserDTO user) {
		int userType = checkUserType(user);
		if(userType == 2 || userType == 3) return true;
		return false;
	}

	@Override
	public boolean isAdmin(String userId) {
		if(isUser(userId) && getUser(userId).getAuth().equals("Y")) return true;
		return false;
	}
}
