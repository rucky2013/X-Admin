package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Admin;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.enums.LoginType;
import com.leaf.xadmin.exception.GlobalException;
import com.leaf.xadmin.mapper.AdminMapper;
import com.leaf.xadmin.service.IAdminService;
import com.leaf.xadmin.shiro.util.ShiroHelpUtil;
import com.leaf.xadmin.utils.encrypt.PassEncryptUtil;
import com.leaf.xadmin.utils.generator.SnowflakeUtil;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Random;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:35</p>
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private PassEncryptUtil passEncryptUtil;

    @Override
    public Admin queryOne(String name) {
        return baseMapper.selectOne(Admin.builder().name(name).build());
    }

    @Override
    public Serializable addOne(Admin admin) {
        String id = null;
        Random random = new Random();
        SnowflakeUtil idWorker = new SnowflakeUtil(random.nextInt(31), random.nextInt(31));

        if (!ObjectUtils.isEmpty(admin)) {
            Admin oneByName = queryOne(admin.getName());
            if (oneByName != null) {
                throw new GlobalException(ErrorStatus.ACCOUNT_EXIST_ERROR);
            }
            // 生成id
            id = String.valueOf(idWorker.nextId());
            // 设置密钥
            passEncryptUtil.setSecretKey(LoginType.ADMIN.getType() + admin.getName());
            admin.setId(id);
            admin.setPass(passEncryptUtil.encryptPass(admin.getPass()));
            baseMapper.insert(admin);
        }

        return id;
    }

    @Autowired
    private ShiroHelpUtil shiroHelpUtil;

    @Override
    public Collection<Session> queryActiveSessions() {
        return shiroHelpUtil.getActiveSessions();
    }

    @Override
    public boolean forceLogoutByName(String name) {
        return shiroHelpUtil.removeLoginSessionByName(name);
    }

    @Override
    public boolean clearAuthorsByName(String name) {
        return shiroHelpUtil.removeAuthorsByName(name);
    }
}
