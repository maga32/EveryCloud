package com.everycloud.project.controller.file;

import com.everycloud.project.domain.Settings;
import com.everycloud.project.domain.Share;
import com.everycloud.project.service.file.FileService;
import com.everycloud.project.service.file.ShareService;
import com.everycloud.project.service.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShareFileController {
    @Autowired
    SettingsService settingsService;

    @Autowired
    ShareService shareService;

    @Autowired
    FileService fileService;

    @RequestMapping(value="/shareNewFile", method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> shareNewFile(@RequestParam("path") String path,
           @RequestParam("fileName") String fileName) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        boolean validPath = fileService.isPathExist(path+"/"+fileName);

        if(validPath) {
            File nowPath = fileService.getFile(path+"/"+fileName);
            // windows folder path processing
            path = path.replaceAll("\\\\", "/");
            String realPath = nowPath.getCanonicalPath().replaceAll("\\\\", "/");

            Share sharedFile = shareService.getShareByPath(realPath);
            if(sharedFile == null) sharedFile = shareService.createShare(realPath);

            map.put("sharedFile", sharedFile);
            map.put("sharedFullLink", addSlash(settingsService.getSettings("admin").getExternalUrl()) + "file?shareLink=" + sharedFile.getLink());
            map.put("result", "ok");
        } else {
            map.put("result", "잘못된 경로입니다.");
        }

        return map;
    }

    private String addSlash(String url) {
        if(!url.endsWith("/")) url += "/";
        return url;
    }

    @RequestMapping(value="/inputSharePass", method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> shareNewFile(HttpSession session,
            @RequestParam("sharePass") String sharePass) {
        Map<String, Object> map = new HashMap<String, Object>();
        session.setAttribute("sharePass", sharePass);

        map.put("result", "ok");
        return map;
    }

}
