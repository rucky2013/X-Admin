package com.leaf.xadmin.service.impl;

import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author leaf
 * <p>date: 2018-01-01 18:05</p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceImplTest {

    @Autowired
    private IUserService userService;

    @Test
    public void queryUser() throws Exception {
        User user = userService.queryOneByName("leaf");
        log.info(user.toString());
    }
}