package com.project.everycloud.service;

import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface ShareService {

    ShareDTO getShareByLink(String link);

    ShareDTO getShareByPath(String path);

    ShareDTO createShare(String realPath);

    ShareGroupDTO getShareGroup(String shareLink, Integer groupNo);

    /**
     * Check user's authentication for shareLink and return share path
     *
     * @param shareLink shareLink
     * @param authType 0: read<br>1: write
     * @return Map(String, String) :<br>
     * (String) invalidAuth : if does not have auth, returns invalidAuth and reason<br>
     * (String) needPassword : if does not have auth with need Password, returns needPassword and reason<br>
     * (String) invalidString : if there is any reason of reject, invalidString is exist<br>
     * (String) sharePath : if user has valid auth, returns share path
     */
    Map<String, String> getShareAuth(String shareLink, int authType, HttpSession session);
}
