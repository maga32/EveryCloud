package com.everycloud.project.service.user;

import com.everycloud.project.domain.User;

public interface UserService {
	
	User getUser(String userId);

	String checkUser(String userId, String userPass);
	
}
