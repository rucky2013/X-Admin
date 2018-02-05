package com.leaf.xadmin.controller;

import com.leaf.xadmin.vo.ErrorTemplateVO;
import com.leaf.xadmin.vo.ResponseResultVO;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.exception.*;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-01-13 21:11</p>
 */
@Api(value = "系统错误请求", description = "/fail")
@RestControllerAdvice
@RequestMapping("error")
@Slf4j
public class ErrorController extends AbstractErrorController {
    @Value("${server.fail.path:${fail.path:/fail}}")
    private static String errorPath = "/fail";
    private ErrorAttributes attributes;

    @Autowired
    public ErrorController(ErrorAttributes attributes) {
        super(attributes);
        this.attributes = attributes;
    }

    @Override
    public String getErrorPath() {
        return errorPath;
    }

    @ApiOperation(value = "系统内部错误")
    @RequestMapping
    public ResponseResultVO globalException(HttpServletRequest request) {
        ErrorTemplateVO template;
        // 获取系统自定义错误参数
        Map<String, Object> errorAttributes = this.getErrorAttributes(request, false);
        // 获取自定义异常参数
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable exception = attributes.getError(requestAttributes).getCause();
        if (exception instanceof GlobalException) {
            // 转换为自定义错误枚举类型
            ErrorStatus status = ((GlobalException) exception).getStatus();
            template = ErrorTemplateVO.builder()
                    .error(status.getError())
                    .code(status.getCode())
                    .message(status.getMessage())
                    .exception(exception.getClass().getName())
                    .path(errorAttributes.get("path").toString())
                    .build();
        } else {
            // 转换为系统未知异常
            template = ErrorTemplateVO.builder()
                    .error(ErrorStatus.SYS_INNER_ERROR.getError())
                    .code(ErrorStatus.SYS_INNER_ERROR.getCode())
                    .message(ErrorStatus.SYS_INNER_ERROR.getMessage())
                    .exception(exception.getClass().getName())
                    .path(errorAttributes.get("path").toString())
                    .build();
        }

        return ResponseResultUtil.fail(template);
    }

    @ApiOperation(value = "用户未登陆")
    @ExceptionHandler({UnauthenticatedException.class})
    @GetMapping("authen")
    public ResponseResultVO unauthenticatedException(UnauthenticatedException e) {
        return ResponseResultUtil.fail(ErrorStatus.AUTHEN_LACK_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "用户名或密码错误")
    @ExceptionHandler({UnknownAccountException.class, IncorrectCredentialsException.class})
    @GetMapping("login")
    public ResponseResultVO unknownAccountException(UnknownAccountException e) {
        return ResponseResultUtil.fail(ErrorStatus.LOGIN_FAIL_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "账户被锁定")
    @ExceptionHandler({LockedAccountException.class})
    @GetMapping("locked")
    public ResponseResultVO lockedAccountException(AuthenticationException e) {
        return ResponseResultUtil.fail(ErrorStatus.ACCOUNT_LOCK_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "尚未授权，无法操作")
    @ExceptionHandler({UnauthorizedException.class})
    @GetMapping("author")
    public ResponseResultVO unauthorizedException(UnauthorizedException e) {
        return ResponseResultUtil.fail(ErrorStatus.AUTHOR_LACK_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "无解析token编码支持")
    @ExceptionHandler({TokenDecodeException.class})
    @GetMapping("decode")
    public ResponseResultVO unsupportedEncodingException(TokenDecodeException e) {
        return ResponseResultUtil.fail(ErrorStatus.TOKEN_DECODE_ERROR, e.getClass().getName());
    }


    @ApiOperation(value = "HTTP请求方法错误")
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @GetMapping("method")
    public ResponseResultVO httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseResultUtil.fail(ErrorStatus.HTTP_METHOD_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "缺少请求参数")
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @GetMapping("params")
    public ResponseResultVO missingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseResultUtil.fail(ErrorStatus.MISS_PARAMETER_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "重复登录")
    @ExceptionHandler({RepeatLoginException.class})
    @GetMapping("repeat")
    public ResponseResultVO repeatLoginException(RepeatLoginException e) {
        return ResponseResultUtil.fail(ErrorStatus.REPEAT_LOGIN_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "强制退出")
    @ExceptionHandler({ForceLogoutException.class})
    @GetMapping("force")
    public ResponseResultVO forceLogoutException(ForceLogoutException e) {
        return ResponseResultUtil.fail(ErrorStatus.FORCE_LOGOUT_ERROR, e.getClass().getName());
    }
}
