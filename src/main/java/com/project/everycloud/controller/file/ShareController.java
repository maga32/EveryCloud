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

    @PostMapping("/shareSimpleNewFile")
    public AppResponse<String> shareSimpleNewFile(@Valid @RequestBody NewFileDTO shareNewFile) {

        String sharedFullLink = shareService.shareSimpleNewFile(shareNewFile, sessionUser());

        return new AppResponse<String>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(sharedFullLink);
    }

    @PostMapping("/inputSharePass")
    public AppResponse<Void> inputSharePass(@RequestParam("sharePass") String sharePass) {

        session.setAttribute("user", shareService.inputSharePass(sharePass, sessionUser()));

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }

    /* ----- share list start ----- */
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
    public AppResponse<String> shareNewFile(@Valid @RequestBody ShareDTO shareNewFile) {

        String sharedFullLink = shareService.shareNewFile(shareNewFile, sessionUser());

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
    /* ----- share list end ----- */


    /* ----- share group start ----- */
    @PostMapping("/groupList")
    public AppResponse<AppList<ShareGroupDTO>> getGroupList(@RequestBody HashMap<String, Object> paramMap) {

        AppList<ShareGroupDTO> shareList = shareService.getGroupList(paramMap, sessionUser());

        return new AppResponse<AppList<ShareGroupDTO>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareList);
    }

    @PostMapping("/groupInfo")
    public AppResponse<AppList<UserDTO>> getGroupInfo(@RequestBody HashMap<String, Object> paramMap) {

        AppList<UserDTO> shareInfo = shareService.getGroupInfo(paramMap, sessionUser());

        return new AppResponse<AppList<UserDTO>>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message())
                .setData(shareInfo);
    }

    @PostMapping("/groupUpdate")
    public AppResponse<Void> groupUpdate(@RequestBody ShareGroupDTO shareGroup) {

        shareService.groupUpdate(shareGroup, sessionUser());

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }

    @PostMapping("/groupDelete")
    public AppResponse<Void> groupDelete(@RequestParam("groupNo") String groupNo) {

        shareService.groupDelete(groupNo, sessionUser());

        return new AppResponse<Void>()
                .setCode(ResponseType.SUCCESS.code())
                .setMessage(ResponseType.SUCCESS.message());
    }
    /* ----- share group end ----- */

    private UserDTO sessionUser() {
        return (UserDTO) session.getAttribute("user");
    }
}
