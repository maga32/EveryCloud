package com.project.everycloud.service.mapper;

import com.project.everycloud.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {

	UserDTO getUser(String id);

	List<UserDTO> getUserList(HashMap<String, Object> paramMap);

	UserDTO getUserInfo(String id);

	String getPass(String id);

    UserDTO getAdmin();

	void updateUser(HashMap<String, Object> paramMap);
}
