package com.everycloud.project.service.file;

import com.everycloud.project.domain.Share;

public interface ShareService {

    Share getShareByLink(String link);

    Share getShareByPath(String path);

    Share createShare(String realPath);
}
