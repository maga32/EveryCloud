package com.everycloud.project.service.file;

import com.everycloud.project.domain.Share;
import com.everycloud.project.domain.ShareGroup;

import java.util.Map;

public interface ShareService {

    Share getShareByLink(String link);

    Share getShareByPath(String path);

    Share createShare(String realPath);

    ShareGroup getShareGroup(String shareLink, Integer groupNo);

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
    Map<String, String> getShareAuth(String shareLink, int authType);
}
