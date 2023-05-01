package com.everycloud.project.service.file;

import com.everycloud.project.domain.Share;
import com.everycloud.project.domain.ShareGroup;

public interface ShareService {

    Share getShareByLink(String link);

    Share getShareByPath(String path);

    Share createShare(String realPath);

    ShareGroup getShareGroup(String shareLink, Integer groupNo);
}
