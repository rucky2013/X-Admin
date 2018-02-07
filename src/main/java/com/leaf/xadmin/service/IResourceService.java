package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Permission;
import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.entity.Role;

import java.sql.SQLDataException;
import java.util.List;
import java.util.Set;

/**
 * @author leaf
 * <p>date: 2018-02-04 20:48</p>
 * <p>version: 1.0</p>
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 批量添加资源记录列表
     *
     * @param resourceList
     */
    boolean addBatch(List<Resource> resourceList);

    /**
     * 批量添加或更新资源记录列表
     *
     * @param resourceList
     */
    boolean addOrUpdateBatch(List<Resource> resourceList);

    /**
     * 根据路径查询资源信息
     *
     * @param path
     * @return
     * @throws SQLDataException
     */
    Resource queryOneByPath(String path);

    /**
     * 根据路径查询角色信息列表
     *
     * @param path
     * @return
     */
    Set<Role> queryRolesByPath(String path);

    /**
     * 根据路径查询权限信息列表
     *
     * @param path
     * @return
     */
    Set<Permission> queryPermissionsByPath(String path);
}
