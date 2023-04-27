package com.everycloud.project.service.settings;

import com.everycloud.project.dao.settings.SettingsDao;
import com.everycloud.project.domain.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsDao settingsDao;

    @Override
    public List<Settings> getSettings() {
        return settingsDao.getSettings();
    }

    @Override
    public Settings getSettings(String type) {
        return settingsDao.getSettings(type);
    }
}
