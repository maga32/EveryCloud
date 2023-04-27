package com.everycloud.project.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everycloud.project.domain.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public User getUser(String id) {
		return sqlSession.selectOne("userMapper.getUser", id);
	}

	@Override
	public String getUserPass(String id) {
		return  sqlSession.selectOne("userMapper.getUserPass", id);
	}

	@Override
	public User getAdmin() {
		return sqlSession.selectOne("userMapper.getAdmin");
	}

	@Override
	public void updateUser(User user, String userOrigId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", user);
		map.put("userOrigId", userOrigId);
		sqlSession.update("userMapper.updateUser", map);
	}

}
