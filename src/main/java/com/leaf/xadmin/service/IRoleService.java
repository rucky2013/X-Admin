package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Role;

import java.util.List;

/**
 * @author leaf
 * <p>date: 2018-02-04 20:38</p>
 * <p>version: 1.0</p>
 */
public interface IRoleService extends IService<Role> {
    /**
     * 查询用户角色列表
     *
     * @param name
     * @return
     */
    List<Role> queryUserRoles(String name);

    /**
     * 查询管理员角色列表
     *
     * @param name
     * @return
     */
    List<Role> queryAdminRoles(String name);
}
