package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-01-05 21:27</p>
 */
@Getter
@AllArgsConstructor
public enum AdminStatus {
    // 正常管理员
    NORMAL(1, "正常", "可登录"),
    INVALID(0, "无效", "无法登陆");

    private Integer code;
    private String name;
    private String desc;
}
