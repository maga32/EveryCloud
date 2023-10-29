package com.project.everycloud.service.mapper;

import com.project.everycloud.model.response.settings.SettingsDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SettingsMapper {
    List<SettingsDTO> getSettings();
    SettingsDTO getSettings(String type);
}
