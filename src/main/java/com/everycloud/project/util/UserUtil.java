package com.everycloud.project.util;

import com.everycloud.project.domain.User;
import com.everycloud.project.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;

@Controller
public class UserUtil {
    @Autowired
    UserService userService;

    @Autowired
    HttpSession session;

    /**
     * Check whether admin or not by session
     *
     * @return boolean
     */
    public boolean isAdmin() {
        int userType = checkUserType();
        if(userType == 2 || userType == 3) return true;
        return false;
    }

    /**
     * Check whether admin or not by userId
     *
     * @param userId
     * @return boolean
     */
    public boolean isAdmin(String userId) {
        if(isUser(userId) && userService.getUser(userId).getUserAuthority().equals("Y")) return true;
        return false;
    }

    /**
     * Check whether user or not by session
     *
     * @return boolean
     */
    public boolean isUser() {
        int userType = checkUserType();
        if(userType != 0) return true;
        return false;
    }

    /**
     * Check whether user or not by userId
     *
     * @param userId
     * @return boolean
     */
    public boolean isUser(String userId) {
        if(userService.getUser(userId) != null) return true;
        return false;
    }

    /**
     * Check whether user or not by matching userId and userPass
     *
     * @param userId
     * @param userPass
     * @return boolean
     */
    public boolean isUser(String userId, String userPass) {
        BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
        if(pass.matches(userPass, userService.getUserPass(userId))) return true;
        return false;
    }

    /**
     * Check userId is matching with session's userId
     *
     * @param userId
     * @return boolean
     */
    public boolean isValidUser(String userId) {
        User user = (User) session.getAttribute("user");
        if(user != null && userId.equals(user.getUserId())) return true;
        return false;
    }

    /**
     * return user type by session's user
     *
     * @return int<br>
     * 0. not logged in<br>
     * 1. user<br>
     * 2. admin<br>
     * 3. admin (who needs to change the password)
     */
    public int checkUserType() {
        int userType = 0;
        User user = (User) session.getAttribute("user");
        User admin = userService.getAdmin();

        if(user != null) {
            if(user.getUserId().equals(admin.getUserId())) {
                userType = 2;
            } else {
                userType = 1;
            }
        } else if(admin.getUserPass().equals("admin")) {
            userType = 3;
        }

        return userType;
    }
}
