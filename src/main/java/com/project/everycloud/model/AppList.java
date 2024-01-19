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
 * @param <O> Option Object
 */
@Data
@ToString
@NoArgsConstructor
@Accessors (chain = true)
public class AppList<L ,O> {
    // list count
    private long total;

    // content
    private List<L> lists;

    // option
    private O option;
}
