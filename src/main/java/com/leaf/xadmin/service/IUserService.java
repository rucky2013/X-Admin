package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.enums.UserStatus;

import java.io.Serializable;
import java.util.List;

/**
 * @author leaf
 * <p>date: 2017-12-31 1:47</p>
 */
public interface IUserService extends IService<User> {

    /**
     * 添加用户(若添加成功返回id)
     *
     * @param user
     * @return
     */
    Serializable addOne(User user);

    /**
     * 用户登出(清理自定义缓存信息)
     *
     * @param name
     * @return
     */
    void logout(String name);

    /**
     * 查询用户信息
     *
     * @param id
     * @return
     */
    User queryOneById(String id);

    /**
     * 查询用户信息
     *
     * @param name
     * @return
     */
    User queryOneByName(String name);

    /**
     * 查询用户列表
     *
     * @return
     */
    List<User> queryList();

    /**
     * 查询指定类型用户列表
     *
     * @param type
     * @return
     */
    List<User> queryListByType(Integer type);

    /**
     * 查询指定状态用户列表
     *
     * @param status
     * @return
     */
    List<User> queryListByStatus(Integer status);

}
