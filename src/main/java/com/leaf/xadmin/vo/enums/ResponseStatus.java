package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-02-02 16:21:37</p>
 */
@Getter
@AllArgsConstructor
public enum ResponseStatus {
    SUCCESS(1, "success"),
    FAIL(0, "fail");

    private Integer status;
    private String message;
}
