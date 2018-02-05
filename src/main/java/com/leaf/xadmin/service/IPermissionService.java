package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Permission;

import java.util.List;

/**
 * @author leaf
 * <p>date: 2018-02-04 20:38</p>
 * <p>version: 1.0</p>
 */
public interface IPermissionService extends IService<Permission> {
    /**
     * 查询用户权限列表
     *
     * @param name
     * @return
     */
    List<Permission> queryPermissions(String name);
}
