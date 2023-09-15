package com.project.everycloud.model.share;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
