package com.project.everycloud.service.mapper;

import com.project.everycloud.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface UserMapper {

	UserDTO getUser(String id);

	UserDTO getUserInfo(String id);

	String getPass(String id);

    UserDTO getAdmin();

	void updateUser(HashMap<String, Object> paramMap);
}
