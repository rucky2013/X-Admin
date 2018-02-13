package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-02-06 22:30</p>
 * <p>version: 1.0</p>
 */
@Getter
@AllArgsConstructor
public enum LoginType {
    // 登录类型
    ADMIN("Admin"),
    USER("User"),
    ;

    private String type;
}
