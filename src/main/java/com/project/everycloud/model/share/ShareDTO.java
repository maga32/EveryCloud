package com.project.everycloud.model.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {
    private String origLink;
    @NotBlank(message = "필수항목: link")
    private String link;
    @NotBlank(message = "필수항목: path")
    private String path;
    private Timestamp date;
    private Integer method;
    private String pass;
    private Integer auth;
    private boolean exist;
    private List<ShareGroupDTO> shareGroupList;
}
