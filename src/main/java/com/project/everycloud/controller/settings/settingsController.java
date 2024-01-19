package com.project.everycloud.controller.settings;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.settings.SettingsDTO;
import com.project.everycloud.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
public class settingsController {

    @Autowired
    SettingsService settingsService;

    @GetMapping("/getMeta")
    public AppResponse<SettingsDTO> getMeta() {

        SettingsDTO settings = settingsService.getMeta();

        return new AppResponse<SettingsDTO>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(settings);
    }

    @PostMapping("/getSetting")
    public AppResponse<String> getSetting(@RequestParam("setting") String setting) {

        String setValue = settingsService.getSpecificSetting(setting);

        return new AppResponse<String>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(setValue);
    }
}
