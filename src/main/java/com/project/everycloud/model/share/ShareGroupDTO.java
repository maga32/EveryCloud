package com.project.everycloud.model.share;

import com.project.everycloud.model.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ShareGroupDTO {
    private String shareLink;
    private Integer groupNo;
    private String groupName;
    private Integer auth;
    private List<UserDTO> shareUserList;
}
