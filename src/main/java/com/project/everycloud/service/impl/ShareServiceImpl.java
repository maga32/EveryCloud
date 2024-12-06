package com.project.everycloud.service.impl;

import com.project.everycloud.common.exception.*;
import com.project.everycloud.common.util.FileUtil;
import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.file.NewFileDTO;
import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;
import com.project.everycloud.service.SettingsService;
import com.project.everycloud.service.ShareService;
import com.project.everycloud.service.UserService;
import com.project.everycloud.service.mapper.FileDao;
import com.project.everycloud.service.mapper.ShareMapper;
import com.project.everycloud.service.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    UserMapper userMapper;

    @Override
    public AppList<ShareDTO> getShareList(HashMap<String, Object> paramMap, UserDTO sessionUser) {

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        AppList<ShareDTO> share = new AppList<ShareDTO>();
        List<ShareDTO> tempList =  shareMapper.getShareList(paramMap);
        List<ShareDTO> shareList =  new ArrayList<ShareDTO>();

        for(ShareDTO tempShare : tempList) {
            tempShare.setExist(fileDao.isPathExist(tempShare.getPath()));
            shareList.add(tempShare);
        }

        share.setLists(shareList);
        share.setOption(settingsService.getSettings("admin").getExternalUrl());

        return share;
    }

    @Override
    public AppList<ShareGroupDTO> getShareInfo(HashMap<String, Object> paramMap, UserDTO sessionUser) {

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        AppList<ShareGroupDTO> result = new AppList<ShareGroupDTO>();
        List<ShareGroupDTO> groupList = shareMapper.getShareGroupList(paramMap);
        HashMap<String, Object> shareMap = new HashMap<String, Object>();

        ShareDTO share = shareMapper.getShareByLink((String) paramMap.get("shareLink"));
        share.setExist(fileDao.isPathExist(share.getPath()));
        shareMap.put("share", share);
        shareMap.put("externalUrl", settingsService.getSettings("admin").getExternalUrl());

        result.setLists(groupList);
        result.setOption(shareMap);

        return result;
    }

    @Override
    public AppList<ShareGroupDTO> getShareNewInfo(UserDTO sessionUser) {

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        AppList<ShareGroupDTO> result = new AppList<ShareGroupDTO>();
        HashMap<String, Object> shareMap = new HashMap<String, Object>();

        List<ShareGroupDTO> groupList = shareMapper.getShareGroupList(shareMap);
        shareMap.put("link", createNewShareLink());
        shareMap.put("externalUrl", settingsService.getSettings("admin").getExternalUrl());

        result.setLists(groupList);
        result.setOption(shareMap);

        return result;
    }

    @Override
    @Transactional
    public String shareSimpleNewFile(NewFileDTO shareNewFile, UserDTO sessionUser) {

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        String result = "";
        String path = shareNewFile.getPath();

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

    @Override
    @Transactional
    public String shareNewFile(ShareDTO shareNewFile, UserDTO sessionUser) {

        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        String result = "";
        String path = shareNewFile.getPath();

        if(fileDao.isPathExist(path)) {
            File nowPath = fileDao.getFile(path);
            String realPath;
            try {
                realPath = FileUtil.macPath(nowPath.getCanonicalPath());
            } catch (IOException e) {
                realPath = FileUtil.macPath(nowPath.getPath());
            }

            // already exist share check
            ShareDTO pathCheck = shareMapper.getShareByPath(realPath);
            if(pathCheck != null) throw new DuplicateKeyException("error");
            ShareDTO linkCheck = shareMapper.getShareByLink(shareNewFile.getLink());
            if(linkCheck != null) throw new DuplicateKeyException("error");

            // create new share
            shareNewFile.setOrigLink(createShare(realPath).getLink());

            // update new share
            shareUpdate(shareNewFile, sessionUser);

            result = shareNewFile.getLink();

        } else {
            throw new InvalidPathException("Error","error");
        }

        return result;
    }

    private ShareDTO createShare(String realPath) {
        String link = createNewShareLink();
        ShareDTO newShare = new ShareDTO();
        newShare.setLink(link);
        newShare.setPath(realPath);
        newShare.setMethod(0);
        newShare.setAuth(0);

        shareMapper.createShare(newShare);

        return newShare;
    }

    private String createNewShareLink() {
        String link = "";
        while(true) {
            link = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            if (shareMapper.getShareByLink(link) == null) break;
        }
        return link;
    }

    @Override
    @Transactional
    public void shareUpdate(ShareDTO share, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        // share group setting
        shareMapper.deleteShareGroup(share);
        if(share.getShareGroupList() != null && !share.getShareGroupList().isEmpty()) {
            for(ShareGroupDTO shareGroup : share.getShareGroupList()) {
                shareMapper.insertShareGroup(shareGroup);
            }
        }

        // share password setting
        if(share.getMethod() == 1) {
            String oldPass = shareMapper.getSharePassByLink(share.getOrigLink());
            if(!StringUtils.hasText(oldPass) && !StringUtils.hasText(share.getPass())) {
                throw new NeedPasswordException();
            } else if(!StringUtils.hasText(share.getPass())) {
                share.setPass(oldPass);
            } else {
                BCryptPasswordEncoder pass = new BCryptPasswordEncoder(10);
                share.setPass(pass.encode(share.getPass()));
            }
        }

        try {
            shareMapper.updateShare(share);
        } catch(UncategorizedSQLException e) {
            if(e.getSQLException().toString().contains("SQLITE_CONSTRAINT_UNIQUE")) throw new DuplicateKeyException("");
        }
    }

    @Override
    @Transactional
    public void shareDelete(String link, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        // delete share group
        ShareDTO share = new ShareDTO();
        share.setOrigLink(link);
        shareMapper.deleteShareGroup(share);

        // delete share
        shareMapper.deleteShare(link);
    }

    @Override
    public ShareGroupDTO getShareGroup(String shareLink, Integer groupNo) {
        return shareMapper.getShareGroup(shareLink, groupNo);
    }

    @Override
    public UserDTO inputSharePass(String sharePass, UserDTO sessionUser) {
        sessionUser = sessionUser != null ? sessionUser : new UserDTO();
        sessionUser.setSharePass(sharePass);

        return sessionUser;
    }


    @Override
    public AppList<ShareGroupDTO> getGroupList(HashMap<String, Object> paramMap, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        AppList<ShareGroupDTO> result = new AppList<ShareGroupDTO>();
        List<ShareGroupDTO> groupList = shareMapper.getGroupList(paramMap);
        result.setLists(groupList);

        return result;
    }

    @Override
    public AppList<UserDTO> getGroupInfo(HashMap<String, Object> paramMap, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        AppList<UserDTO> result = new AppList<UserDTO>();
        HashMap<String, Object> shareMap = new HashMap<String, Object>();

        // all user list
        List<UserDTO> groupList = userService.getAllUserList(paramMap);
        result.setLists(groupList);

        // when Group is not new one, get the group name
        String groupName = ((Integer) paramMap.get("groupNo") == 0) ? "" : shareMapper.getGroupList(paramMap).get(0).getGroupName();
        shareMap.put("groupName", groupName);
        result.setOption(shareMap);

        return result;
    }

    @Override
    @Transactional
    public void groupUpdate(ShareGroupDTO shareGroup, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        try {
            int result = (shareGroup.getGroupNo() == 0)
                       ? shareMapper.insertNewGroup(shareGroup)
                       : shareMapper.updateGroup(shareGroup);
        } catch(UncategorizedSQLException e) {
            if(e.getSQLException().toString().contains("SQLITE_CONSTRAINT_UNIQUE")) throw new DuplicateKeyException("");
        }

        List<UserDTO> users = shareGroup.getShareUserList();

        // if user list exist, update group of user
        if(users != null && !users.isEmpty()) {
            for(UserDTO user : users) {
                if(user.getGroupNo() == 0) user.setGroupNo(shareGroup.getGroupNo());
                HashMap<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("user", user);
                userMapper.updateUser(paramMap);
            }
        }
    }

    @Override
    @Transactional
    public void groupDelete(String groupNo, UserDTO sessionUser) {
        if(!userService.isAdmin(sessionUser)) throw new NotAllowedException();

        if(groupNo == null || groupNo.isEmpty() || groupNo.equals("1")) throw new BadRequestException();

        // update user group to default group
        userMapper.updateUserGroupToDefault(groupNo);

        // delete group
        shareMapper.deleteGroup(groupNo);
    }


    static BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder(10);
    @Override
    public int verifyAuth(String shareLink, int authType, UserDTO sessionUser) {
        int result = 0;
        boolean isValid = false;

        if(userService.isAdmin(sessionUser)) {
            isValid = true;
            result = 1;
        } else if(!StringUtils.hasText(shareLink)) {
            throw new NotAllowedException();
        } else {
            ShareDTO share = getShareByLink(shareLink);
            result = share.getAuth();

            // - auth 0 : read, auth 1 : write
            // method 0 : share for who has the link
            if(share.getMethod() == 0 && !(share.getAuth() == 0 && authType == 1)) {
                isValid = true;
            // method 1 : share for who know the password
            } else if(share.getMethod() == 1 && !(share.getAuth() == 0 && authType == 1)) {
                String sharePass = (sessionUser == null) ? null : sessionUser.getSharePass();
                if(!StringUtils.hasText(sharePass)) {
                    throw new NeedPasswordException();
                } else if(!BCRYPT.matches(sharePass, getSharePassByLink(shareLink))) {
                    throw new InvalidPasswordException();
                }
                isValid = true;

            // method 2 : share for group who has authority
            } else if(share.getMethod() == 2) {
                if(sessionUser != null && StringUtils.hasText(sessionUser.getId())) {
                    ShareGroupDTO shareGroup = getShareGroup(shareLink, sessionUser.getGroupNo());
                    if(shareGroup != null && !(shareGroup.getAuth() == 0 && authType == 1)) {
                        isValid = true;
                        result = shareGroup.getAuth();
                    }
                }
            }
        }

        if(!isValid) throw new NotAllowedException();
        return result;
    }

    @Override
    public ShareDTO getShareByLink(String link) {
        ShareDTO share = shareMapper.getShareByLink(link);
        if(share == null) throw new InvalidLinkException();
        if(share.getDate() != null) {
            Timestamp nowTime = Timestamp.valueOf(LocalDateTime.now());
            if(nowTime.after(share.getDate())) throw new InvalidLinkException();
        }

        return share;
    }

    @Override
    public ShareDTO getShareByPath(String realPath) {
        ShareDTO share = shareMapper.getShareByPath(realPath);
        if(share == null) throw new InvalidPathException("error","error");
        return share;
    }

    @Override
    public String getSharePassByLink(String link) {
        return shareMapper.getSharePassByLink(link);
    }

    @Override
    public String getSharePassByPath(String path) {
        return shareMapper.getSharePassByPath(path);
    }

    private String getFullShareLink(String shareLink) {
        return FileUtil.addSlash(settingsService.getSettings("admin").getExternalUrl()) + "share/link/" + shareLink;
    }

}
