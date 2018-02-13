package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-01-01 14:52</p>
 */
@Getter
@AllArgsConstructor
public enum UserStatus {
    // 正常用户
    NORMAL(1, "正常", "状态正常，可登录"),
    INVALID(0, "无效", "无效用户，无法登录");

    private Integer code;
    private String name;
    private String desc;
}
