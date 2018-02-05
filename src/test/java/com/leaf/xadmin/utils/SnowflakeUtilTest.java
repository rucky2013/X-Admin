package com.leaf.xadmin.utils;

import com.leaf.xadmin.utils.generater.SnowflakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author leaf
 * <p>date: 2018-01-13 15:34</p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SnowflakeUtilTest {
    @Test
    public void nextId() throws Exception {
        SnowflakeUtil idWorker = new SnowflakeUtil(0, 0);
        for (int i = 0; i < 1; i++) {
            long id = idWorker.nextId();
            log.info(id + "");
        }
    }

}