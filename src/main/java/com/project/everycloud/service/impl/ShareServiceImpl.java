package com.project.everycloud.service.impl;

import com.project.everycloud.model.share.ShareDTO;
import com.project.everycloud.model.share.ShareGroupDTO;
import com.project.everycloud.service.ShareService;
import com.project.everycloud.service.mapper.ShareMapper;
import com.project.everycloud.common.util.FileUtil;
import com.project.everycloud.common.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ShareServiceImpl implements ShareService {

    @Autowired
    ShareMapper shareMapper;

    @Lazy
    UserUtil userUtil;

    @Lazy
    FileUtil fileUtil;

    @Override
    public ShareDTO getShareByLink(String link) {
        return shareMapper.getShareByLink(link);
    }

    @Override
    public ShareDTO getShareByPath(String path) {
        return shareMapper.getShareByPath(path);
    }

    @Override
    public ShareDTO createShare(String realPath) {
        String link = "";
        while(true) {
            link = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
            if(getShareByLink(link) == null) break;
        }
        ShareDTO newShare = new ShareDTO(link, realPath, null, 0, null, 0);
        shareMapper.createShare(newShare);

        return newShare;
    }

    @Override
    public ShareGroupDTO getShareGroup(String shareLink, Integer groupNo) {
        return shareMapper.getShareGroup(shareLink, groupNo);
    }

    @Override
    public Map<String, String> getShareAuth(String shareLink, int authType, HttpSession session) {
        Map<String, String> shareMap = new HashMap<String, String>();
        shareMap.put("shareLink", shareLink);
        int hasValidAuth = fileUtil.hasValidAuth(shareLink, authType, session);
        String invalidString = "";

        if(shareLink.equals("") && !userUtil.isAdmin(session)) {
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
                shareMap.put("sharePath", shareMapper.getShareByLink(shareLink).getPath());
            }
        }

        return shareMap;
    }

}
