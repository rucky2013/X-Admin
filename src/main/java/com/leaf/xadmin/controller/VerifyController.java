package com.leaf.xadmin.controller;

import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.vo.enums.ErrorStatus;
import com.leaf.xadmin.vo.exception.GlobalException;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import com.leaf.xadmin.utils.verify.VerifyPictureUtil;
import com.leaf.xadmin.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-02-08 18:46</p>
 * <p>version: 1.0</p>
 */
@Api(value = "验证请求相关", description = "/verify")
@RestController
@RequestMapping("verify")
@CrossOrigin
@Slf4j
public class VerifyController {

    @Autowired
    private VerifyPictureUtil verifyPictureUtil;

    @ApiOperation(value = "生成Base64验证码")
    @PostMapping(value = "generate")
    public ResponseResultVO generate() {
        // 绘制验证图片
        Map<String, Object> resultMap = verifyPictureUtil.draw();

        // 存储验证码至session中
        Session session = SecurityUtils.getSubject().getSession(Boolean.FALSE);
        if (session == null) {
            throw new GlobalException(ErrorStatus.VERIFY_PICTURE_ERROR);
        } else {
            session.setAttribute(GlobalConstants.SESSION_VERIFY_PICTURE_KEY, resultMap.get(VerifyPictureUtil.VERIFY_CODE_VALUE));
        }

        // 返回Base64编码请求
        return ResponseResultUtil.success(resultMap.get(VerifyPictureUtil.VERIFY_ENCODE_RESULT));
    }

    @ApiOperation(value = "校验验证码合法性")
    @PostMapping(value = "valid")
    public ResponseResultVO valid(@RequestParam("code") String code) {
        Session session = SecurityUtils.getSubject().getSession(Boolean.FALSE);
        if (session == null) {
            return ResponseResultUtil.fail(Boolean.FALSE);
        } else {
            String attribute = (String) session.getAttribute(GlobalConstants.SESSION_VERIFY_PICTURE_KEY);
            if (!attribute.isEmpty() && attribute.equalsIgnoreCase(code)) {
                return ResponseResultUtil.fail(Boolean.TRUE);
            }
            return ResponseResultUtil.fail(Boolean.FALSE);
        }
    }
}
