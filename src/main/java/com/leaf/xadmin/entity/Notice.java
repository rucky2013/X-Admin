package com.leaf.xadmin.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author leaf
 * <p>date: 2018-01-12 17:10</p>
 */
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notice {
    private Long id;
    private String name;
    private String desc;
    private Integer type;
    private Integer status;
    private String ip;
    private Long adminId;
    private Date createTime;
    private Date updateTime;
}
