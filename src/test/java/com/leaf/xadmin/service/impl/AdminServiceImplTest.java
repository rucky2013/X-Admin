package com.leaf.xadmin.service.impl;

import com.leaf.xadmin.entity.Admin;
import com.leaf.xadmin.service.IAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceImplTest {

    @Autowired
    private IAdminService adminService;

    @Test
    public void queryOne() {
        Admin admin = adminService.queryOne("admin");
        assertNotNull(admin);
    }
}