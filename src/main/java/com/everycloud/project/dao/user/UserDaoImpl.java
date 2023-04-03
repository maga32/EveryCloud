package com.everycloud.project.dao.user;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.everycloud.project.domain.User;

@Repository
public class UserDaoImpl implements UserDao {
	
	@Autowired
	SqlSession sqlSession;
	
	@Override
	public User getUser(String userId) {
		return sqlSession.selectOne("userMapper.getUser", userId);
	}

	@Override
	public String getUserPass(String userId) {
		return  sqlSession.selectOne("userMapper.getUserPass", userId);
	}

}
