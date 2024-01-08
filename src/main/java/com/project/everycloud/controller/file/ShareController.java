package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.file.NewFileDTO;
import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;
import com.project.everycloud.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/share")
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    HttpSession session;

    @PostMapping("/shareList")
    public AppResponse<AppList<ShareDTO, String>> getShareList(@RequestBody HashMap<String, Object> paramMap) {

        AppList<ShareDTO, String> shareList = shareService.getShareList(paramMap, sessionUser());

        return new AppResponse<AppList<ShareDTO, String>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareList);
    }

    @PostMapping("/shareInfo")
    public AppResponse<AppList<ShareGroupDTO, HashMap<String, Object>>> getShareInfo(@RequestBody HashMap<String, Object> paramMap) {

        AppList<ShareGroupDTO, HashMap<String, Object>> shareInfo = shareService.getShareInfo(paramMap, sessionUser());

        return new AppResponse<AppList<ShareGroupDTO, HashMap<String, Object>>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareInfo);
    }

    @PostMapping("/shareNewFile")
    public AppResponse<String> shareNewFile(@Valid @RequestBody NewFileDTO shareNewFile) {

        String sharedFullLink = shareService.shareNewFile(shareNewFile, sessionUser());

        return new AppResponse<String>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(sharedFullLink);
    }

    @PostMapping("/shareUpdate")
    public AppResponse<Void> shareUpdate(@RequestBody ShareDTO share) {

        shareService.shareUpdate(share, sessionUser());

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }

    @PostMapping("/inputSharePass")
    public AppResponse<Void> inputSharePass(@RequestParam("sharePass") String sharePass) {

        session.setAttribute("user", shareService.inputSharePass(sharePass, sessionUser()));

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }

    private UserDTO sessionUser() {
        return (UserDTO) session.getAttribute("user");
    }
}
