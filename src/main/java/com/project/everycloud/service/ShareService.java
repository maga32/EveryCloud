package com.project.everycloud.service;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.share.ShareDTO;
import com.project.everycloud.model.response.share.ShareGroupDTO;

import java.util.HashMap;

public interface ShareService {

    AppList<ShareDTO, String> getShareList(UserDTO sessionUser, HashMap<String, Object> paramMap);

    AppList<ShareGroupDTO, HashMap<String, Object>> getShareInfo(UserDTO userDTO, HashMap<String, Object> paramMap);

    String shareNewFile(NewFileDTO shareNewFile, UserDTO sessionUser);

    ShareGroupDTO getShareGroup(String shareLink, Integer groupNo);

    UserDTO inputSharePass(String sharePass, UserDTO sessionUser);

    /**
     * Verify user's authentication for shareLink
     *
     * @param shareLink shareLink
     * @param authType 0: read / 1: write
     * @param sessionUser session's UserDTO
     */
    void verifyAuth(String shareLink, int authType, UserDTO sessionUser);

    ShareDTO getShareByLink(String link);

    ShareDTO getShareByPath(String realPath);

    String getSharePassByLink(String link);

    String getSharePassByPath(String path);

}
