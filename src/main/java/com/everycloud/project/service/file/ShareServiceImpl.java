package com.everycloud.project.service.file;

import com.everycloud.project.dao.file.ShareDao;
import com.everycloud.project.domain.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    ShareDao shareDao;

    @Override
    public Share getShareByLink(String link) {
        return shareDao.getShareByLink(link);
    }

    @Override
    public Share getShareByPath(String path) {
        return shareDao.getShareByPath(path);
    }

    @Override
    public Share createShare(String realPath) {
        String link = "";
        while(true) {
            link = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            if(getShareByLink(link) == null) break;
        }
        Share newShare = new Share(link, realPath, null, 0, null, 0);
        shareDao.createShare(newShare);

        return newShare;
    }
}
