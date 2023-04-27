package com.everycloud.project.dao.user;

import com.everycloud.project.domain.User;

public interface UserDao {

	User getUser(String id);

	String getUserPass(String id);

    User getAdmin();

	void updateUser(User user, String userOrigId);
}
