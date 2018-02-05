package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.enums.UserStatus;
import com.leaf.xadmin.mapper.UserMapper;
import com.leaf.xadmin.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author leaf
 * <p>date: 2017-12-31 2:22</p>
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @CacheEvict(key = "#p0", allEntries = true)
    @Override
    public void logout(String name) {}
    
    @Override
    public User queryOneById(String id) {
        return baseMapper.selectUserById(id);
    }

    @Cacheable(key = "#p0")
    @Override
    public User queryOneByName(String name) {
        return baseMapper.selectUserByName(name);
    }

    @Override
    public List<User> queryList() {
        return baseMapper.selectAllUsers();
    }

    @Override
    public List<User> queryListByType(Integer type) {
        return baseMapper.selectList(new EntityWrapper<User>().eq("type", type));
    }

    @Override
    public List<User> queryListByStatus(Integer status) {
        return baseMapper.selectList(new EntityWrapper<User>().eq("status", status));

    }
}
