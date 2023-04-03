package com.everycloud.project.dao.user;

import com.everycloud.project.domain.User;

public interface UserDao {

	User getUser(String userId);

	String getUserPass(String userId);
	
}
