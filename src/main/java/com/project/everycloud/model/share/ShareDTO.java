package com.project.everycloud.model.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {
    private String OrigLink;
    private String link;
    private String path;
    private Timestamp date;
    private Integer method;
    private String pass;
    private Integer auth;
    private boolean exist;
    private List<ShareGroupDTO> shareGroupList;
}
