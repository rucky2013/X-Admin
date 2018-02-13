package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Admin;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:23</p>
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 查询管理员信息
     *
     * @param name
     * @return
     */
    Admin queryOne(String name);

    /**
     * 添加管理员
     *
     * @param admin
     * @return
     */
    Serializable addOne(Admin admin);

    /**
     * 获取存活session
     *
     * @return
     */
    Collection<Session> queryActiveSessions();

    /**
     * 强制用户下线
     *
     * @param name
     * @return
     */
    boolean forceLogoutByName(String name);

    /**
     * 清理指定用户权限缓存
     *
     * @param name
     * @return
     */
    boolean clearAuthorsByName(String name);

}
