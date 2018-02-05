package com.leaf.xadmin.utils;

import com.auth0.jwt.interfaces.Claim;
import com.leaf.xadmin.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JwtUtilTest {

    @Test
    public void generateToken() {
        String token = JwtUtil.generateToken("1", "leaf", "0b84c3197519e5bf");
        log.info(token);
    }

    @Test
    public void verifyToken() {
        String token = JwtUtil.generateToken("1", "leaf", "0b84c3197519e5bf");
        log.info(token);
        Map<String, Claim> claimMap = JwtUtil.verifyToken(token);
        log.info(claimMap.get("pass").asString());
    }
}