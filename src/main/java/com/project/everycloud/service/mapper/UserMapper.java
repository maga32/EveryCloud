package com.project.everycloud.service.mapper;

import com.project.everycloud.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

	UserDTO getUser(String id);

	String getUserPass(String id);

    UserDTO getAdmin();

	void updateUser(UserDTO user, String userOrigId);
}
