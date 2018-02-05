package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Admin;
import com.leaf.xadmin.mapper.AdminMapper;
import com.leaf.xadmin.service.IAdminService;
import com.leaf.xadmin.shiro.util.ShiroHelpUtil;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author leaf
 * <p>date: 2018-01-05 18:35</p>
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

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
