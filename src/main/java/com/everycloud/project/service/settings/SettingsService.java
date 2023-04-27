package com.everycloud.project.service.settings;

import com.everycloud.project.domain.Settings;

import java.util.List;

public interface SettingsService {
    List<Settings> getSettings();

    Settings getSettings(String type);
}
