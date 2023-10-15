package com.project.everycloud.service;

import com.project.everycloud.model.UserDTO;

import java.util.HashMap;

public interface UserService {

	/**
	 * return session's user
	 *
	 * @param user
	 * @return UserDTO
	 */
	UserDTO getSessionUser(UserDTO user);

	/**
	 * return UserDTO by user id
	 *
	 * @param id
	 * @return UserDTO
	 */
	UserDTO getUser(String id);

	/**
	 * return UserDTO(id,nickname,email,auth) by user id
	 *
	 * @param id
	 * @return UserDTO
	 */
	UserDTO getUserInfo(String id);

	/**
	 * return user password by user id
	 *
	 * @param id
	 * @return String
	 */
	String getUserPass(String id);


	/**
	 * Login user and return UserDTO
	 *
	 * @param paramMap
	 * @return UserDTO
	 */
	UserDTO login(HashMap<String, Object> paramMap);

	UserDTO updateUserForm(HashMap<String,Object> paramMap);

	/**
	 * update user and retern updated user info
	 *
	 * @param paramMap
	 * @return UserDTO
	 */
	UserDTO updateUser(HashMap<String, Object> paramMap);

	/**
	 * return user type by session's user
	 *
	 * @param user
	 * @return int<br>
	 * 0. not logged in<br>
	 * 1. user<br>
	 * 2. admin<br>
	 * 3. admin(who needs to change the password)
	 */
	int checkUserType(UserDTO user);

	/**
	 * Check whether user or not by session
	 *
	 * @param user
	 * @return boolean
	 */
	boolean isUser(UserDTO user);

	/**
	 * Check whether user or not by userId
	 *
	 * @param id
	 * @return boolean
	 */
	boolean isUser(String id);

	/**
	 * Check whether user or not by matching userId and userPass
	 *
	 * @param id
	 * @param pass
	 * @return boolean
	 */
	boolean isUser(String id, String pass);

	/**
	 * Check whether admin or not by session
	 *
	 * @param user
	 * @return boolean
	 */
	public boolean isAdmin(UserDTO user);

	/**
	 * Check whether admin or not by userId
	 *
	 * @param id
	 * @return boolean
	 */
	public boolean isAdmin(String id);

}