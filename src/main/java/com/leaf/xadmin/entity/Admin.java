package com.leaf.xadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:51</p>
 */
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Admin {
    private String id;
    private String name;
    private String pass;
    private String phone;
    private String email;
    private String avatar;
    private Integer age;
    private Integer sex;
    private String desc;
    private Integer type;
    private Integer status;
    private Timestamp lastLoginTime;
    private String lastLoginIp;
    private String lastLoginLocal;
    private Date createTime;
    private Date updateTime;
}
