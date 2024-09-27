package com.project.everycloud.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.everycloud.common.exception.*;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.SettingsMapper;
import com.project.everycloud.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	SettingsMapper settingsMapper;

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
	public List<UserDTO> getAllUserList(HashMap<String, Object> paramMap) {
		return userMapper.getAllUserList(paramMap);
	}

	@Override
	public AppList<UserDTO> getUserList(HashMap<String, Object> paramMap, UserDTO sessionUser) {
		if(!isAdmin(sessionUser)) throw new NotAllowedException();

		AppList<UserDTO> result = new AppList<UserDTO>();
		List<UserDTO> userList = userMapper.getUserList(paramMap);

		result.setLists(userList);
		result.setTotal(userList.size());

		return result;
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
	public void createUser(HashMap<String, Object> paramMap, UserDTO sessionUser) {
		boolean isAdmin = isAdmin(sessionUser);
		String allowJoin = settingsMapper.getSettings("admin").getAllowJoin();

		if(!isAdmin && allowJoin.equals("N")) throw new NotAllowedException();

		UserDTO user = new ObjectMapper().convertValue(paramMap.get("user"),UserDTO.class);

		if(userMapper.countExistEmail(paramMap) > 0) {
			throw new ExistEmailException();
		} else if (!isAdmin) {
			user.setNeedVerify("Y");
		} else {
			user.setNeedVerify("N");
		}

		BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
		user.setPass(pass.encode(user.getPass()));

		userMapper.createUser(user);
	}

	@Override
	public UserDTO updateUser(HashMap<String, Object> paramMap, UserDTO sessionUser) {
		UserDTO user = new ObjectMapper().convertValue(paramMap.get("user"),UserDTO.class);
		String origId = paramMap.get("origId").toString();
		boolean isAdmin = isAdmin(sessionUser);
		boolean isUser = isUser(sessionUser);

		// if there is no email or nickname, throw error
		if(!StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getNickname())) {
			throw new BadRequestException();
		}

		// if user is not admin, only can edit own account
		if(!isAdmin && isUser) {
			user.setId("");
			origId = sessionUser.getId();
		} else if(!isAdmin) {
			throw new NotAllowedException();
		}

		UserDTO oldUser = getUser(origId);

		// if the email has changed, check the email exist and change the status need to verify
		if(!oldUser.getEmail().equals(user.getEmail())) {
			if(userMapper.countExistEmail(paramMap) > 0) {
				throw new ExistEmailException();
			} else if(!isAdmin){
				user.setNeedVerify("Y");
			}
		}

		// Encode password
		if(StringUtils.hasText(user.getPass())) {
			BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
			user.setPass(pass.encode(user.getPass()));
		}

		paramMap.put("user", user);
		userMapper.updateUser(paramMap);

		return isAdmin ? getAdmin() : getUser(origId);
	}

	@Override
	public void deleteUser(String userId, UserDTO sessionUser) {
		boolean isAdmin = isAdmin(sessionUser);
		boolean isUser = isUser(sessionUser);
		if(!isAdmin && (!isUser || !sessionUser.getId().equals(userId)) ) throw new NotAllowedException();

		userMapper.deleteUser(userId);
	}


	@Override
	public int checkUserType(UserDTO user) {
		int userType = 0;
		UserDTO admin = getAdmin();

		if(user != null && StringUtils.hasText(user.getId())) {
			userType = admin.getId().equals(user.getId()) ? 2 : 1;
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
