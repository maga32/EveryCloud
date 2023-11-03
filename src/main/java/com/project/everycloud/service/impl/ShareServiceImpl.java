package com.project.everycloud.service.impl;

import com.project.everycloud.common.exception.InvalidLinkException;
import com.project.everycloud.common.exception.InvalidPasswordException;
import com.project.everycloud.common.exception.NeedPasswordException;
import com.project.everycloud.common.exception.NotAllowedException;
import com.project.everycloud.common.util.FileUtil;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.share.ShareDTO;
import com.project.everycloud.model.response.share.ShareGroupDTO;
import com.project.everycloud.service.SettingsService;
import com.project.everycloud.service.ShareService;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.FileDao;
import com.project.everycloud.service.mapper.ShareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.UUID;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    UserService userService;

    @Autowired
    SettingsService settingsService;

    @Autowired
    FileDao fileDao;

    @Autowired
    ShareMapper shareMapper;

    @Override
    public String shareNewFile(NewFileDTO shareNewFile, UserDTO sessionUser) {

        String result = "";
        String path = shareNewFile.getPath();

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        if(StringUtils.hasText(shareNewFile.getShareLink())) {
            path = getShareByLink(shareNewFile.getShareLink()).getPath() + path;
        }

        if(fileDao.isPathExist(path+"/"+shareNewFile.getName())) {
            File nowPath = fileDao.getFile(shareNewFile.getPath()+"/"+shareNewFile.getName());
            String realPath;
            try {
                realPath = FileUtil.macPath(nowPath.getCanonicalPath());
            } catch (IOException e) {
                realPath = FileUtil.macPath(nowPath.getPath());
            }

            ShareDTO shareFile = shareMapper.getShareByPath(realPath);
            String shareLink = (shareFile == null) ? createShare(realPath).getLink() : shareFile.getLink();

            result = getFullShareLink(shareLink);

        } else {
            throw new InvalidPathException("Error","error");
        }

        return result;
    }

    private ShareDTO createShare(String realPath) {
        String link = "";
        while(true) {
            link = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            if(shareMapper.getShareByLink(link) == null) break;
        }
        ShareDTO newShare = new ShareDTO();
        newShare.setLink(link);
        newShare.setPath(realPath);
        newShare.setMethod(0);
        newShare.setAuth(0);

        shareMapper.createShare(newShare);

        return newShare;
    }


    static BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder(10);
    @Override
    public void verifyAuth(String shareLink, int authType, UserDTO sessionUser) {
        boolean isValid = false;

        if(userService.isAdmin(sessionUser)) {
            isValid = true;
        } else if(!StringUtils.hasText(shareLink)) {
            throw new NotAllowedException();
        } else {
            ShareDTO share = getShareByLink(shareLink);

            // - auth 0 : read, auth 1 : write
            // method 0 : share for who has the link
            if(share.getMethod() == 0 && !(share.getAuth() == 0 && authType == 1)) {
                isValid = true;

            // method 1 : share for who know the password
            } else if(share.getMethod() == 1 && !(share.getAuth() == 0 && authType == 1)) {
                String sharePass = (sessionUser == null) ? null : sessionUser.getSharePass();
                if(!StringUtils.hasText(sharePass)) {
                    throw new NeedPasswordException();
                } else if(!BCRYPT.matches(sharePass, share.getPass())) {
                    throw new InvalidPasswordException();
                }
                isValid = true;

            // method 2 : share for group who has authority
            } else if(share.getMethod() == 2) {
                if(sessionUser != null && StringUtils.hasText(sessionUser.getId())) {
                    ShareGroupDTO shareGroup = getShareGroup(shareLink, sessionUser.getGroupNo());
                    if(shareGroup != null && !(shareGroup.getAuth() == 0 && authType == 1)) {
                        isValid = true;
                    }
                }
            }
        }

        if(!isValid) throw new NotAllowedException();
    }


    @Override
    public UserDTO inputSharePass(String sharePass, UserDTO sessionUser) {
        sessionUser = sessionUser != null ? sessionUser : new UserDTO();
        sessionUser.setSharePass(sharePass);

        return sessionUser;
    }


    @Override
    public ShareGroupDTO getShareGroup(String shareLink, Integer groupNo) {
        return shareMapper.getShareGroup(shareLink, groupNo);
    }

    @Override
    public ShareDTO getShareByLink(String link) {
        ShareDTO share = shareMapper.getShareByLink(link);
        if(share == null) throw new InvalidLinkException();
        return share;
    }

    @Override
    public ShareDTO getShareByPath(String realPath) {
        ShareDTO share = shareMapper.getShareByPath(realPath);
        if(share == null) throw new InvalidLinkException();
        return share;
    }

    private String getFullShareLink(String shareLink) {
        return FileUtil.addSlash(settingsService.getSettings("admin").getExternalUrl()) + "file?shareLink=" + shareLink;
    }

}
