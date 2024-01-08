package com.project.everycloud.model.share;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShareGroupDTO {
    private String shareLink;
    private Integer groupNo;
    private String groupName;
    private Integer auth;
}
