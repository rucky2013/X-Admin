package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author leaf
 * <p>date: 2018-01-21 17:53</p>
 * <p>version: 1.0</p>
 */
@Getter
@AllArgsConstructor
public enum RequestStatus {
    // 请求类型
    REQUEST(0, "request", "request"),
    POST(1, "post", "post"),
    GET(2, "get", "get"),
    PUT(3, "put", "put"),
    DELETE(4, "delete", "delete");

    private Integer code;
    private String name;
    private String desc;

}
