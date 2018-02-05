package com.leaf.xadmin.utils;

import com.leaf.xadmin.utils.redis.JedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisUtilTest {

    @Test
    public void setExSet() {
        int result = JedisUtil.setExSet("auth_flush:", 10000, "leaf", "sss");
        assertNotEquals(0, result);
    }
}