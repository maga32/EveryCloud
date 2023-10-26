package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.FileListLoadDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.file.FileDetailDTO;
import com.project.everycloud.model.response.file.FileOptionDTO;
import com.project.everycloud.service.FileService;
import com.project.everycloud.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/share")
public class ShareController {

    @Autowired
    FileService fileService;

    @Autowired
    ShareService shareService;

    @Autowired
    HttpSession session;

    @PostMapping("/shareNewFile")
    public AppResponse<String> shareNewFile(@Valid @RequestBody NewFileDTO shareNewFile) {

        UserDTO sessionUser = (UserDTO) session.getAttribute("user");
        String sharedFullLink = shareService.shareNewFile(shareNewFile, sessionUser);

        return new AppResponse<String>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(sharedFullLink);
    }

    /*------------------- 수정필요 ------------------*/
    @RequestMapping(value="/shareNewFile2", method= RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> shareNewFile2(@RequestParam("path") String path,
                                           @RequestParam("fileName") String fileName) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();

        /*
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
        */

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
