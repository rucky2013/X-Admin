package com.leaf.xadmin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.leaf.xadmin.entity.Account;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.vo.enums.ErrorStatus;
import com.leaf.xadmin.vo.enums.LoginType;
import com.leaf.xadmin.vo.exception.GlobalException;
import com.leaf.xadmin.mapper.UserMapper;
import com.leaf.xadmin.service.IAccountService;
import com.leaf.xadmin.service.IUserService;
import com.leaf.xadmin.utils.encrypt.PassEncryptUtil;
import com.leaf.xadmin.utils.generator.SnowflakeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * @author leaf
 * <p>date: 2017-12-31 2:22</p>
 */
@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private PassEncryptUtil passEncryptUtil;

    @Autowired
    private IAccountService accountService;

    @Override
    public Serializable addOne(User user) {
        String id = null;
        Random random = new Random();
        SnowflakeUtil idWorker = new SnowflakeUtil(random.nextInt(31), random.nextInt(31));

        if (!ObjectUtils.isEmpty(user)) {
            User oneByName = queryOneByName(user.getName());
            if (oneByName != null) {
                throw new GlobalException(ErrorStatus.ACCOUNT_EXIST_ERROR);
            }
            // 生成id
            id = String.valueOf(idWorker.nextId());
            // 设置密钥
            passEncryptUtil.setSecretKey(LoginType.USER.getType() + user.getName());
            user.setId(id);
            user.setPass(passEncryptUtil.encryptPass(user.getPass()));
            if (baseMapper.insert(user).equals(1)) {
                Account account = Account.builder()
                        .id(id)
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .nickname(user.getName())
                        .build();
                accountService.addOne(account);
            } else {
                throw new GlobalException(ErrorStatus.SQL_EXECUTE_ERROR);
            }
        }

        return id;

    }

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
