package com.project.everycloud.common.util;

import com.project.everycloud.model.UserDTO;
import com.project.everycloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    @Autowired
    UserService userService;

    /**
     * Check whether admin or not by session
     *
     * @param user
     * @param admin
     * @return boolean
     */
    public boolean isAdmin(UserDTO user, UserDTO admin) {
        int userType = checkUserType(user, admin);
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
        if(isUser(userId) && userService.getUser(userId).getAuth().equals("Y")) return true;
        return false;
    }

    /**
     * Check whether user or not by session
     *
     * @return boolean
     */
    public boolean isUser(UserDTO user, UserDTO admin) {
        int userType = checkUserType(user, admin);
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
    public boolean isValidUser(UserDTO user, String userId) {
        if(user != null && userId.equals(user.getId())) return true;
        return false;
    }

    /**
     * return user type by session's user
     *
     * @param user
     * @param admin
     * @return int<br>
     * 0. not logged in<br>
     * 1. user<br>
     * 2. admin<br>
     * 3. admin (who needs to change the password)
     */
    public int checkUserType(UserDTO user, UserDTO admin) {
        int userType = 0;

        if(user != null) {
            if(user.getId().equals(admin.getId())) {
                userType = 2;
            } else {
                userType = 1;
            }
        } else if(admin.getPass().equals("admin")) {
            userType = 3;
        }

        return userType;
    }
}
