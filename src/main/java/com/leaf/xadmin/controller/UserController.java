package com.leaf.xadmin.controller;

import com.leaf.xadmin.vo.ResponseResultVO;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.exception.RepeatLoginException;
import com.leaf.xadmin.service.IUserService;
import com.leaf.xadmin.utils.jwt.JwtUtil;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author leaf
 * <p>date: 2017-12-31 1:47</p>
 */
@Api(value = "用户请求相关", description = "/user")
@RestController
@RequestMapping("user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "获取指定id用户信息")
    @GetMapping(value = "getUser/{id}")
    public ResponseResultVO getUserById(@PathVariable("id") String id) {
        User user = userService.queryOneById(id);
        return user != null ? ResponseResultUtil.success(user) : ResponseResultUtil.fail();
    }

    @ApiOperation(value = "获取指定id用户信息")
    @GetMapping(value = "getUser/{name}")
    public ResponseResultVO getUserByName(@PathVariable("name") String name) {
        return ResponseResultUtil.success(userService.queryOneByName(name));
    }


    @ApiOperation(value = "用户登录")
    @PostMapping(value = "login")
    public ResponseResultVO login(@RequestParam("name") String name, @RequestParam("pass") String pass, HttpServletResponse response) throws Exception {
        String loginToken; // 登录凭证
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(name, pass);
            subject.login(token);
            User user = userService.queryOneByName(name);
            loginToken = JwtUtil.generateToken(user.getId(), name, user.getPass());
            // token写入cookie
            response.addCookie(new Cookie("token", loginToken));
            return ResponseResultUtil.success(loginToken);
        } else {
            // 判断再次请求登陆时，是否与已保存的session信息一致
            if (SecurityUtils.getSubject().getPrincipal().toString().equals(name)) {
                throw new RepeatLoginException(ErrorStatus.REPEAT_LOGIN_ERROR);
            } else {
                logout();
                return login(name, pass, response);
            }
        }
    }

    @ApiOperation(value = "用户退出")
    @PostMapping(value = "logout")
    public ResponseResultVO logout() {
        // 清除自定义用户信息缓存
        userService.logout(SecurityUtils.getSubject().getPrincipal().toString());
        // 清除用户权限缓存
        SecurityUtils.getSubject().logout();
        return ResponseResultUtil.success(true);
    }
}
