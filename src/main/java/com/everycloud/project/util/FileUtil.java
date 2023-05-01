package com.everycloud.project.util;

import com.everycloud.project.domain.Share;
import com.everycloud.project.domain.ShareGroup;
import com.everycloud.project.domain.User;
import com.everycloud.project.service.file.ShareService;
import com.everycloud.project.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;

@Controller
public class FileUtil {

    static BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);

    @Autowired
    UserService userService;

    @Autowired
    ShareService shareService;

    @Autowired
    HttpSession session;

    /**
     * Check user's authentication for shareLink
     *
     * @return boolean
     */
    public boolean isValidAuth(String shareLink) {
        Share share = shareService.getShareByLink(shareLink);
        if(share.getMethod() == 0) {
            return true;
        } else if(share.getMethod() == 1) {
            if(pass.matches((String) session.getAttribute("sharePass"), share.getPass())) {
                return true;
            }
        } else if (share.getMethod() == 2) {
            User user = (User) session.getAttribute("user");
            if(user != null) {
                ShareGroup shareGroup = shareService.getShareGroup(shareLink, user.getGroupNo());
                if (shareGroup != null) return true;
            }
        }
        return false;
    }

}
