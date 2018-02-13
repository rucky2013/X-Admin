package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-02-08 1:17</p>
 * <p>version: 1.0</p>
 */
@Getter
@AllArgsConstructor
public enum SexType {
    // 性别类型
    MALE(1, "男"),
    FEMALE(0, "女"),
    UNKNOWN(-1, "未知")
    ;

    private Integer code;
    private String name;
}
