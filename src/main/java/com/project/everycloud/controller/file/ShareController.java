package com.project.everycloud.controller.file;

import com.project.everycloud.common.type.ResponseType;
import com.project.everycloud.model.AppResponse;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/share")
public class ShareController {

    @Autowired
    ShareService shareService;

    @Autowired
    HttpSession session;

    @PostMapping("/shareNewFile")
    public AppResponse<String> shareNewFile(@Valid @RequestBody NewFileDTO shareNewFile) {

        String sharedFullLink = shareService.shareNewFile(shareNewFile, sessionUser());

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

    private UserDTO sessionUser() {
        return (UserDTO) session.getAttribute("user");
    }
}
