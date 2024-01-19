package com.project.everycloud.service;

import com.project.everycloud.model.settings.SettingsDTO;

import java.util.List;

public interface SettingsService {
    List<SettingsDTO> getSettings();

    SettingsDTO getSettings(String type);

    String getSpecificSetting(String column);

    SettingsDTO getMeta();

    void setPort(int port);
}
