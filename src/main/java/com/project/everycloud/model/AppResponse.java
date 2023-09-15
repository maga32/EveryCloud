package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@NoArgsConstructor
@Accessors (chain = true)
public class AppResponse<T> {
    // response code
    private String code;

    // resonse message
    private String message;

    // @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;
}
