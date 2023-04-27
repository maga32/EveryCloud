package com.everycloud.project.service.user;

import com.everycloud.project.domain.User;

public interface UserService {
	
	User getUser(String id);

	User getAdmin();

	String getUserPass(String id);

	String checkUser(String id, String pass);

	void updateUser(User user, String userOrigId);
}
