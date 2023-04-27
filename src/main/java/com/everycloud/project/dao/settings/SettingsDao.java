package com.everycloud.project.dao.settings;

import com.everycloud.project.domain.Settings;

import java.util.List;

public interface SettingsDao {
    List<Settings> getSettings();
    Settings getSettings(String type);
}
