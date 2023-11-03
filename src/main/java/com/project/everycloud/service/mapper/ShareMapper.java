package com.project.everycloud.service.mapper;

import com.project.everycloud.model.response.share.ShareDTO;
import com.project.everycloud.model.response.share.ShareGroupDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareMapper {

    ShareDTO getShareByLink(String link);

    ShareDTO getShareByPath(String path);

    void createShare(ShareDTO newShare);

    ShareGroupDTO getShareGroup(String shareLink, Integer groupNo);
}
