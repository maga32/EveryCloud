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
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/share")
public class ShareController {

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
