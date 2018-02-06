package com.leaf.xadmin.service.impl;

import com.leaf.xadmin.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MailServiceImplTest {

    @Autowired
    private IMailService mailService;

    @Test
    public void sendSimpleMail() {
        mailService.sendSimpleMail("3273104264@qq.com", "测试邮件", "测试内容!!!");
    }
}