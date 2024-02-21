package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * return List type
 *
 * @param <L> ListDTO
 */
@Data
@ToString
@NoArgsConstructor
@Accessors (chain = true)
public class AppList<L> {
    // list count
    private long total;

    // content
    private List<L> lists;

    // option
    private Object option;
}
