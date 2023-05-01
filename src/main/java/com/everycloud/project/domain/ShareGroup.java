package com.everycloud.project.domain;

import java.sql.Timestamp;

public class ShareGroup {
    private String shareLink;
    private Integer groupNo;
    private Integer auth;

    public ShareGroup() {}

    public ShareGroup(String shareLink, Integer groupNo, Integer auth) {
        this.shareLink = shareLink;
        this.groupNo = groupNo;
        this.auth = auth;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public Integer getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(Integer groupNo) {
        this.groupNo = groupNo;
    }

    public Integer getAuth() {
        return auth;
    }

    public void setAuth(Integer auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return "ShareGroup{" +
                "shareLink='" + shareLink + '\'' +
                ", groupNo=" + groupNo +
                ", auth=" + auth +
                '}';
    }
}
