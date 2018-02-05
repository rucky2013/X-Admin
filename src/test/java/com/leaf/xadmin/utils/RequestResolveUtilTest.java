package com.leaf.xadmin.utils;

import com.leaf.xadmin.controller.UserController;
import com.leaf.xadmin.utils.request.RequestResolveUtil;
import com.leaf.xadmin.vo.RequestResourceVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RequestResolveUtilTest {

    @Autowired
    private RequestResolveUtil resolveUtil;

    @Test
    public void methodResolver() throws Exception {
        resolveUtil.methodResolver(UserController.class.getMethod("getUser", String.class));
    }

    @Test
    public void pathMerge() throws Exception {
        List<RequestResourceVO> resourceList = resolveUtil.methodResolver(UserController.class.getMethod("getUser", String.class));
        RequestResourceVO requestResourceVO = resourceList.get(0);
        String pathMerge = RequestResolveUtil.pathMerge(requestResourceVO.getParentPath(), requestResourceVO.getChildPath());
        log.debug(pathMerge);
    }

}