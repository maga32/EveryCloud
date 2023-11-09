package com.project.everycloud.service.mapper;

import com.project.everycloud.model.response.share.ShareDTO;
import com.project.everycloud.model.response.share.ShareGroupDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface ShareMapper {

    List<ShareDTO> getShareList(HashMap<String, Object> paramMap);

    ShareDTO getShareByLink(String link);

    ShareDTO getShareByPath(String path);

    String getSharePassByLink(String link);

    String getSharePassByPath(String path);

    void createShare(ShareDTO newShare);

    ShareGroupDTO getShareGroup(String shareLink, Integer groupNo);

    List<ShareGroupDTO> getShareGroupList(HashMap<String, Object> paramMap);
}
