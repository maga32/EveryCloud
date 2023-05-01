package com.everycloud.project.dao.file;

import com.everycloud.project.domain.Share;
import com.everycloud.project.domain.ShareGroup;

public interface ShareDao {

    Share getShareByLink(String link);

    Share getShareByPath(String path);

    void createShare(Share newShare);

    ShareGroup getShareGroup(String shareLink, Integer groupNo);
}
