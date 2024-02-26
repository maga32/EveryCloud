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
    public AppResponse<AppList<ShareDTO>> getShareList(@RequestBody HashMap<String, Object> paramMap) {

        AppList<ShareDTO> shareList = shareService.getShareList(paramMap, sessionUser());

        return new AppResponse<AppList<ShareDTO>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareList);
    }

    @PostMapping("/shareInfo")
    public AppResponse<AppList<ShareGroupDTO>> getShareInfo(@RequestBody HashMap<String, Object> paramMap) {

        AppList<ShareGroupDTO> shareInfo = shareService.getShareInfo(paramMap, sessionUser());

        return new AppResponse<AppList<ShareGroupDTO>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareInfo);
    }

    @PostMapping("/shareNewInfo")
    public AppResponse<AppList<ShareGroupDTO>> getShareNewInfo() {

        AppList<ShareGroupDTO> shareInfo = shareService.getShareNewInfo(sessionUser());

        return new AppResponse<AppList<ShareGroupDTO>>()
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

    @PostMapping("/shareNewDetailFile")
    public AppResponse<String> shareNewDetailFile(@Valid @RequestBody ShareDTO shareNewFile) {

        String sharedFullLink = shareService.shareNewDetailFile(shareNewFile, sessionUser());

        return new AppResponse<String>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(sharedFullLink);
    }

    @PostMapping("/shareUpdate")
    public AppResponse<Void> shareUpdate(@Valid @RequestBody ShareDTO share) {

        shareService.shareUpdate(share, sessionUser());

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }

    @PostMapping("/shareDelete")
    public AppResponse<Void> shareDelete(@RequestParam("link") String link) {

        shareService.shareDelete(link, sessionUser());

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
