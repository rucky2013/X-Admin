package com.leaf.xadmin.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author leaf
 * <p>date: 2018-01-02 21:58</p>
 */
@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 全局错误
    GLOBAL_ERROR(0, "0000", "全局错误", "系统全局异常"),
    AUTHOR_LACK_ERROR(1, "1000", "权限错误", "未授权操作，请求失败"),
    AUTHEN_LACK_ERROR(1, "1001", "权限错误", "未登录，请先进行登录操作"),
    TOKEN_EXPIRE_ERROR(1, "1002", "权限错误", "登录凭证已过期失效，请重新登录"),
    LOGIN_FAIL_ERROR(1, "1003", "权限错误", "用户名或密码错误，登录失败"),
    REPEAT_LOGIN_ERROR(1, "1004", "权限错误", "已登录，请勿重复请求登录"),
    ACCOUNT_LOCK_ERROR(1, "1005", "权限错误", "账户已被锁定，请联系管理员"),
    TOKEN_INVALID_ERROR(1, "1006", "权限错误", "登陆凭证无效，请重新获取"),
    TOKEN_DECODE_ERROR(1, "1007", "权限错误", "凭证解码错误，请联系管理员"),
    FORCE_LOGOUT_ERROR(1, "1008", "权限错误", "用户已被管理员强制下线，请重新登录"),
    ACCOUNT_EXIST_ERROR(1, "1009", "权限错误", "账户已存在，请重新注册"),
    HTTP_METHOD_ERROR(2, "2001", "HTTP错误", "请求姿势不对，要不换个试试----(｡◕‿◕｡)----"),
    MISS_PARAM_ERROR(2, "2002", "HTTP错误", "缺少请求参数，请填写完整请求参数"),
    PARAM_INVALID_ERROR(2, "2003", "HTTP错误", "参数验证无效，请规范输入"),
    PARAM_NO_MATCH_ERROR(2, "2004", "HTTP错误", "无法匹配请求参数，请检查后重试"),
    SYS_UNKNOWN_ERROR(3, "3001", "系统错误", "检测到系统未知异常，请联系管理员"),
    SYS_INNER_ERROR(3, "3002", "系统错误", "系统内部运行错误，系尚未识别异常"),
    MAIL_SEND_ERROR(3, "3003", "系统错误", "邮件发送失败，请检查确认"),
    VERIFY_PICTURE_ERROR(3, "3004", "系统错误", "验证码生成错误，请稍候重试"),
    SQL_DATA_ERROR(4, "4001", "数据库错误", "数据库数据错误，请检查异常点"),
    SQL_EXECUTE_ERROR(4, "4002", "数据库错误", "SQL语句执行失败，请先排查异常"),
    ;

    private Integer type;
    private String code;
    private String error;
    private String message;
}
