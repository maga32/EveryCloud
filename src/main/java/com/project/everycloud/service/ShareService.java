package com.project.everycloud.service;

import com.project.everycloud.model.UserDTO;
import com.project.everycloud.model.request.file.NewFileDTO;
import com.project.everycloud.model.response.share.ShareDTO;
import com.project.everycloud.model.response.share.ShareGroupDTO;

public interface ShareService {

    String shareNewFile(NewFileDTO shareNewFile, UserDTO sessionUser);

    ShareGroupDTO getShareGroup(String shareLink, Integer groupNo);

    ShareDTO getShareByLink(String link);

    ShareDTO getShareByPath(String realPath);

    /**
     * Verify user's authentication for shareLink
     *
     * @param shareLink shareLink
     * @param authType 0: read / 1: write
     * @param sessionUser session's UserDTO
     */
    void verifyAuth(String shareLink, int authType, UserDTO sessionUser);

}
