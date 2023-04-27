package com.everycloud.project.dao.file;

import com.everycloud.project.domain.Share;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ShareDaoImpl implements ShareDao {

    @Autowired
    SqlSession sqlSession;

    @Override
    public Share getShareByLink(String link) {
        return sqlSession.selectOne("shareMapper.getShareByLink", link);
    }

    @Override
    public Share getShareByPath(String path) {
        return sqlSession.selectOne("shareMapper.getShareByPath", path);
    }

    @Override
    public void createShare(Share newShare) {
        sqlSession.insert("shareMapper.createShare", newShare);
    }
}
