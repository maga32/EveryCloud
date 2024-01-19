package com.project.everycloud.service;

import com.project.everycloud.model.AppList;
import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.file.NewFileDTO;
import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;

import java.util.HashMap;

public interface ShareService {

    AppList<ShareDTO, String> getShareList(HashMap<String, Object> paramMap, UserDTO sessionUser);

    AppList<ShareGroupDTO, HashMap<String, Object>> getShareInfo(HashMap<String, Object> paramMap, UserDTO sessionUser);

    String shareNewFile(NewFileDTO shareNewFile, UserDTO sessionUser);

    void shareUpdate(ShareDTO share, UserDTO sessionUser);

    void shareDelete(String link, UserDTO sessionUser);

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
