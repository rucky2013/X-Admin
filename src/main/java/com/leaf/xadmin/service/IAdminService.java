package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Admin;
import com.leaf.xadmin.enums.AdminStatus;
import org.apache.shiro.session.Session;

import java.util.Collection;
import java.util.List;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:23</p>
 */
public interface IAdminService extends IService<Admin> {

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
