package com.everycloud.project.service.file;

import com.everycloud.project.dao.file.ShareDao;
import com.everycloud.project.domain.Share;
import com.everycloud.project.domain.ShareGroup;
import com.everycloud.project.util.FileUtil;
import com.everycloud.project.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    ShareDao shareDao;

    @Autowired
    UserUtil userUtil;

    @Autowired
    FileUtil fileUtil;

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

    @Override
    public ShareGroup getShareGroup(String shareLink, Integer groupNo) {
        return shareDao.getShareGroup(shareLink, groupNo);
    }

    @Override
    public Map<String, String> getShareAuth(String shareLink, int authType) {
        Map<String, String> shareMap = new HashMap<String, String>();
        shareMap.put("shareLink", shareLink);
        int hasValidAuth = fileUtil.hasValidAuth(shareLink, authType);
        String invalidString = "";

        if(shareLink.equals("") && !userUtil.isAdmin()) {
            invalidString = "관리자만 접근할 수 있습니다.";
            shareMap.put("invalidAuth", invalidString);
            shareMap.put("invalidString", invalidString);
        } else if(!shareLink.equals("")) {
            if(hasValidAuth == 0) {
                invalidString = "공유링크가 잘못되었거나 접근 권한이 없습니다.";
                shareMap.put("invalidAuth", invalidString);
                shareMap.put("invalidString", invalidString);
            } else if(hasValidAuth == 2) {
                invalidString = "패스워드 입력이 필요한 서비스입니다.";
                shareMap.put("needPassword", invalidString);
                shareMap.put("invalidString", invalidString);
            } else if(hasValidAuth == 1) {
                shareMap.put("sharePath", shareDao.getShareByLink(shareLink).getPath());
            }
        }

        return shareMap;
    }

}
