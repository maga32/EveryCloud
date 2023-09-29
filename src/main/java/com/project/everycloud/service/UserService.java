package com.project.everycloud.service;

import com.project.everycloud.model.UserDTO;

import javax.servlet.http.HttpSession;

public interface UserService {
	
	UserDTO getUser(String id);

	UserDTO getAdmin();

	String getUserPass(String id);

	String checkUser(String id, String pass);

	void updateUser(UserDTO user, String userOrigId);

	UserDTO getSessionUser(HttpSession session);
}
