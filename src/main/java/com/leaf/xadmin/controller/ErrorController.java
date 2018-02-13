package com.leaf.xadmin.controller;

import com.leaf.xadmin.vo.enums.ErrorStatus;
import com.leaf.xadmin.vo.exception.GlobalException;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import com.leaf.xadmin.vo.ErrorTemplateVO;
import com.leaf.xadmin.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpMethod;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Set;

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
            // 转换为自定义错误异常
            ErrorStatus status = ((GlobalException) exception).getStatus();
            template = ErrorTemplateVO.builder()
                    .error(status.getError())
                    .code(status.getCode())
                    .message(status.getMessage())
                    .exception(exception.getClass().getName())
                    .path(errorAttributes.get("path").toString())
                    .build();
        } else {
            // 转换为未识别异常
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

    @ApiOperation(value = "尚未授权，无法操作")
    @ExceptionHandler({UnauthorizedException.class})
    @GetMapping("author")
    public ResponseResultVO unauthorizedException(UnauthorizedException e) {
        return ResponseResultUtil.fail(ErrorStatus.AUTHOR_LACK_ERROR, e.getClass().getName());
    }

    @ApiOperation(value = "HTTP请求方法错误")
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @GetMapping("method")
    public ResponseResultVO httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        Set<HttpMethod> supportedHttpMethods = e.getSupportedHttpMethods();
        HttpMethod next = supportedHttpMethods.iterator().next();
        return ResponseResultUtil.fail(ErrorStatus.HTTP_METHOD_ERROR, e.getClass().getName(), next.name());
    }

    @ApiOperation(value = "缺少请求参数")
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @GetMapping("miss_params")
    public ResponseResultVO missingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseResultUtil.fail(ErrorStatus.MISS_PARAM_ERROR, e.getClass().getName(), e.getParameterName());
    }

    @ApiOperation(value = "简单参数验证无效")
    @GetMapping("invalid_param")
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseResultVO constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        ConstraintViolation<?> next = constraintViolations.iterator().next();
        return ResponseResultUtil.fail(ErrorStatus.PARAM_INVALID_ERROR, e.getClass().getName(), next.getMessage());
    }

    @ApiOperation(value = "复杂参数验证无效")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @GetMapping("invalid_params")
    public ResponseResultVO methodArgumentNotValidException(MethodArgumentNotValidException e) {
        ObjectError next = e.getBindingResult().getAllErrors().iterator().next();
        return ResponseResultUtil.fail(ErrorStatus.PARAM_INVALID_ERROR, e.getClass().getName(), next.getDefaultMessage());
    }

    @ApiOperation(value = "参数类型不匹配")
    @ExceptionHandler(TypeMismatchException.class)
    @GetMapping("no_match_params")
    public ResponseResultVO typeMismatchException(TypeMismatchException e) {
        return ResponseResultUtil.fail(ErrorStatus.PARAM_NO_MATCH_ERROR, e.getClass().getName(), e.getPropertyName() + ":" + e.getRequiredType());
    }
}
