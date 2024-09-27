package com.project.everycloud.service.mapper;

import com.project.everycloud.model.UserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {

	UserDTO getUser(String id);

	List<UserDTO> getAllUserList(HashMap<String, Object> paramMap);

	List<UserDTO> getUserList(HashMap<String, Object> paramMap);

	UserDTO getUserInfo(String id);

	String getPass(String id);

    UserDTO getAdmin();

	void createUser(UserDTO user);

	void updateUser(HashMap<String, Object> paramMap);

	void deleteUser(String id);

    void updateUserGroupToDefault(String groupNo);

    int countExistEmail(HashMap<String, Object> paramMap);
}
