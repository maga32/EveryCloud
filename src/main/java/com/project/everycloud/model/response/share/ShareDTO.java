package com.project.everycloud.model.response.share;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareDTO {
    private String link;
    private String path;
    private Timestamp date;
    private Integer method;
    private String pass;
    private Integer auth;
    private boolean exist;
}
