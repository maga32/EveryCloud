package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@Accessors (chain = true)
public class AppList<T> {
    // list count
    private long total;

    // content
    private List<T> content;
}
