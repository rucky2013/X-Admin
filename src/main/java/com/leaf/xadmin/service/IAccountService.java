package com.leaf.xadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.leaf.xadmin.entity.Account;

import java.io.Serializable;

/**
 * @author leaf
 * <p>date: 2018-02-08 23:39</p>
 * <p>version: 1.0</p>
 */
public interface IAccountService extends IService<Account> {

    /**
     * 添加账户信息
     *
     * @param account
     * @return
     */
    Serializable addOne(Account account);
}
