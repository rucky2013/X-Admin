package com.leaf.xadmin.utils.serializer;

import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.utils.serializer.SerializeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author leaf
 * <p>date: 2018-01-13 16:38</p>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JsonTest {
    @Test
    public void serializer() throws Exception {
        User user = User.builder().name("ss").pass("123456").id("1").build();
        log.info(SerializeUtil.Json.serializer(user).toString());
    }

    @Test
    public void deserialize() throws Exception {
        User user = User.builder().name("ss").pass("123456").id("1").build();
        User u = SerializeUtil.Json.deserializer(SerializeUtil.Json.serializer(user), User.class);
        log.info(u.toString());
    }

}