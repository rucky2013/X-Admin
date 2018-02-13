package com.leaf.xadmin.utils.encrypt;

import com.leaf.xadmin.vo.enums.LoginType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author leaf
 * <p>date: 2018-01-12 20:58</p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PassEncryptUtilTest {

    @Autowired
    private PassEncryptUtil passEncryptUtil;

    @Test
    public void decryptPass() throws Exception {
        passEncryptUtil.setSecretKey(LoginType.USER.getType() + "xbc");
        log.info(passEncryptUtil.decryptPass(passEncryptUtil.encryptPass("123456")));
    }

    @Test
    public void encryptPass() throws Exception {
        passEncryptUtil.setSecretKey(LoginType.ADMIN.getType() + "admin");
        log.info(passEncryptUtil.encryptPass("123456"));
    }

}