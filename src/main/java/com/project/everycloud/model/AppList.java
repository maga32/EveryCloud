package com.project.everycloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * return List type
 *
 * @param <T1> ListDTO
 * @param <T2> Option Object
 */
@Data
@ToString
@NoArgsConstructor
@Accessors (chain = true)
public class AppList<T1 ,T2> {
    // list count
    private long total;

    // content
    private List<T1> lists;

    // option
    private T2 option;
}
