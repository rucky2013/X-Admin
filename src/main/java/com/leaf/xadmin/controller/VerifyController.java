package com.leaf.xadmin.controller;

import com.leaf.xadmin.utils.response.ResponseResultUtil;
import com.leaf.xadmin.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leaf
 * <p>date: 2018-02-08 18:46</p>
 * <p>version: 1.0</p>
 */
@Api(value = "验证请求相关", description = "/valid")
@RestController
@RequestMapping("valid")
@CrossOrigin
@Slf4j
public class ValidateController {


    public ResponseResultVO checkPicture() {
        // 进行绘制验证图片
        String drawCycleImageToBase64 = CycleImageUtil.drawCycleImageToBase64();

        // 返回Base64编码请求
        return ResponseResultUtil.success(drawCycleImageToBase64);
    }
}
