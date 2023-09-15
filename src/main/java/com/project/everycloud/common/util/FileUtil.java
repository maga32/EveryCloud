package com.project.everycloud.common.util;

import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.ShareService;
import com.project.everycloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class FileUtil {

    static BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);

    @Autowired
    UserService userService;

    @Autowired
    ShareService shareService;

    @Autowired
    HttpSession session;

    @Autowired
    UserUtil userUtil;

    /**
     * Check user's authentication for shareLink
     *
     * @param shareLink shareLink
     * @param authType 0: read<br>1: write
     * @return int<br>
     * 0: invalid authorization<br>
     * 1: valid authorization<br>
     * 2: need password for authorization
     */
    public int hasValidAuth(String shareLink, int authType) {
        ShareDTO share = shareService.getShareByLink(shareLink);

        if(share != null) { // if share link is valid
            if(userUtil.isAdmin()) {
                return 1;
            } else {
                if (share.getMethod() == 0 && !(share.getAuth() == 0 && authType == 1)) {    // share for who has the link
                    return 1;
                } else if (share.getMethod() == 1 && !(share.getAuth() == 0 && authType == 1)) { // share for who know the password
                    String sharePass = (String) session.getAttribute("sharePass");
                    if(sharePass == null || sharePass.equals("") || !pass.matches(sharePass, share.getPass())) return 2;
                    return 1;
                } else if (share.getMethod() == 2) {    // share for group who has authority
                    UserDTO user = (UserDTO) session.getAttribute("user");
                    if (user != null) {
                        ShareGroupDTO shareGroup = shareService.getShareGroup(shareLink, user.getGroupNo());
                        if (shareGroup != null && !(shareGroup.getAuth() == 0 && authType == 1)) {
                            return 1;
                        }
                    }
                }
            }
        }
        return 0;
    }

}
