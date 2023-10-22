package com.project.everycloud.service.impl;

import com.project.everycloud.model.settings.SettingsDTO;
import com.project.everycloud.service.SettingsService;
import com.project.everycloud.service.mapper.SettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void setPort(int port) {
        String filePath = System.getProperty("user.home") + File.separator + ".everyCloud" + File.separator + "config.yml";

        try {
            Yaml yaml = new Yaml();
            Reader yamlFile = new FileReader(filePath);
            Map<String, Object> yamlData = yaml.load(yamlFile);
            Map<String, Object> portData = new HashMap<String, Object>();
            portData.put("port", port);
            yamlData.put("server", portData);

            FileWriter writer = new FileWriter(filePath);
            Representer representer = new Representer();
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml newYaml = new Yaml(representer, options);

            newYaml.dump(yamlData, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
