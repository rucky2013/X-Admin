package com.leaf.xadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:25</p>
 */
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private String id;
    private String nickname;
    private String phone;
    private String email;
    private String avatar;
    private Integer age;
    private Integer sex;
    private String desc;
    private String qq;
    private String wechat;
    private String realname;
    private String identity;
    private Date createTime;
    private Date updateTime;
}
