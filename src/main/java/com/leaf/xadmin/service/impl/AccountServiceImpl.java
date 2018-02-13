package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Account;
import com.leaf.xadmin.mapper.AccountMapper;
import com.leaf.xadmin.service.IAccountService;
import com.leaf.xadmin.vo.enums.ErrorStatus;
import com.leaf.xadmin.vo.exception.GlobalException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author leaf
 * <p>date: 2018-02-08 23:41</p>
 * <p>version: 1.0</p>
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

    @Override
    public Serializable addOne(Account account) {
        String id = null;
        if (!ObjectUtils.isEmpty(account) && !StringUtils.isEmpty(account.getId())) {
            Account selectOne = baseMapper.selectOne(Account.builder().id(account.getId()).build());
            if (selectOne != null) {
                throw new GlobalException(ErrorStatus.ACCOUNT_EXIST_ERROR);
            }
            id = account.getId();
            baseMapper.insert(account);
        }
        return id;
    }

}
