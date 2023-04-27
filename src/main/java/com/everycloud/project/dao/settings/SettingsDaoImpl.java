package com.everycloud.project.dao.settings;

import com.everycloud.project.domain.Settings;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SettingsDaoImpl implements SettingsDao {

    @Autowired
    SqlSession sqlSession;

    @Override
    public List<Settings> getSettings() {
        return sqlSession.selectList("settingsMapper.getSettings");
    }

    @Override
    public Settings getSettings(String type) {
        return sqlSession.selectOne("settingsMapper.getSettings", type);
    }
}
