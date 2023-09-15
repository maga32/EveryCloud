package com.project.everycloud.service.impl;

import com.project.everycloud.model.settings.SettingsDTO;
import com.project.everycloud.service.SettingsService;
import com.project.everycloud.service.mapper.SettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    SettingsMapper settingsMapper;

    @Override
    public List<SettingsDTO> getSettings() {
        return settingsMapper.getSettings();
    }

    @Override
    public SettingsDTO getSettings(String type) {
        return settingsMapper.getSettings(type);
    }
}
