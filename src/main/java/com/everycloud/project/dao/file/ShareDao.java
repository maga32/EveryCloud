package com.everycloud.project.dao.file;

import com.everycloud.project.domain.Share;

public interface ShareDao {

    Share getShareByLink(String link);

    Share getShareByPath(String path);

    void createShare(Share newShare);
}
