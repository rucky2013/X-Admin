package com.leaf.xadmin.mapper;

import com.leaf.xadmin.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author leaf
 * <p>date: 2018-02-04 20:34</p>
 * <p>version: 1.0</p>
 */
@Mapper
@Repository
public interface PermissionMapper extends SuperMapper<Permission> {
    /**
     * 查询用户权限列表
     *
     * @param name
     * @return
     */
    List<Permission> selectUserPermissions(String name);

    /**
     * 查询管理员权限列表
     *
     * @param name
     * @return
     */
    List<Permission> selectAdminPermissions(String name);
}
