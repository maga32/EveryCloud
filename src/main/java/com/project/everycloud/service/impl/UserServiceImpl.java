package com.project.everycloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.everycloud.common.exception.NeedAdminException;
import com.project.everycloud.common.exception.NeedLoginException;
import com.project.everycloud.common.exception.InvalidLoginException;
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

	/**
	 * return admin data
	 *
	 * @return UserDTO
	 */
	private UserDTO getAdmin() {
		return userMapper.getAdmin();
	}

	@Override
	public UserDTO getUser(String id) {
		return userMapper.getUser(id);
	}

	@Override
	public UserDTO getUserInfo(String id) {
		return userMapper.getUserInfo(id);
	}

	@Override
	public String getUserPass(String id) {
		return userMapper.getPass(id);
	}


	@Override
	public UserDTO login(HashMap<String, Object> paramMap) {
		UserDTO user = new UserDTO();
		String id = paramMap.get("id").toString();
		String pass = paramMap.get("pass").toString();

		if (isUser(id, pass)) {
			user = userMapper.getUser(id);
		} else {
			throw new InvalidLoginException();
		}

		return user;
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
			if (userType == 3) returnUser.setPass("admin");
		} else if(isAdmin(user) || user.getId().equals(id)) {
			returnUser = userMapper.getUser(id);
		}

		return returnUser;
	}

	@Override
	public UserDTO updateUser(HashMap<String, Object> paramMap) {
		UserDTO sessionUser = (UserDTO) paramMap.get("sessionUser");
		UserDTO user = new ObjectMapper().convertValue(paramMap.get("user"),UserDTO.class);

		try {
			if(isAdmin(sessionUser) || isUser(sessionUser) && sessionUser.getId().equals(user.getId())) {
				// Encode password
				if(StringUtils.hasText(user.getPass())) {
					BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
					user.setPass(pass.encode(user.getPass()));
				}
				paramMap.put("user", user);
				userMapper.updateUser(paramMap);
			}
		} catch (Exception e) {
			throw new NeedLoginException();
		}

		return isAdmin(sessionUser) ? getAdmin() : getUser(user.getId());
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
		} else if(userMapper.getPass(admin.getId()).equals("admin")) {
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
	public boolean isUser(String id) {
		if(getUser(id) != null) return true;
		return false;
	}

	@Override
	public boolean isUser(String id, String pass) {
		BCryptPasswordEncoder encodePass = new BCryptPasswordEncoder(10);
		if(encodePass.matches(pass, userMapper.getPass(id))) return true;
		return false;
	}

	@Override
	public boolean isAdmin(UserDTO user) {
		int userType = checkUserType(user);
		if(userType == 2 || userType == 3) return true;
		return false;
	}

	@Override
	public boolean isAdmin(String id) {
		if(isUser(id) && getUser(id).getAuth().equals("Y")) return true;
		return false;
	}
}