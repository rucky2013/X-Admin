package com.leaf.xadmin.controller;

import com.leaf.xadmin.vo.enums.LoginType;
import com.leaf.xadmin.other.shiro.token.ExtendedUsernamePasswordToken;
import com.leaf.xadmin.vo.ResponseResultVO;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.service.IUserService;
import com.leaf.xadmin.utils.jwt.JwtUtil;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import com.leaf.xadmin.vo.form.UserRegisterInfoForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "login")
    public ResponseResultVO login(@RequestParam("name") String name, @RequestParam("pass") String pass, HttpServletResponse response) throws Exception {
        String loginToken; // 登录凭证
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(name, pass, LoginType.USER.getType());
            subject.login(token);
            User user = userService.queryOneByName(name);
            loginToken = JwtUtil.generateToken(user.getId(), name, user.getPass());
            // token写入cookie
            response.addCookie(new Cookie("token", loginToken));
            return ResponseResultUtil.success(loginToken);
        } else {
            logout();
            return login(name, pass, response);
        }
    }

    @ApiOperation(value = "用户退出")
    @PostMapping(value = "logout")
    public ResponseResultVO logout() {
        // 清除自定义用户信息缓存
        userService.logout(SecurityUtils.getSubject().getPrincipal().toString());
        // 清除用户权限缓存
        SecurityUtils.getSubject().logout();
        return ResponseResultUtil.success(Boolean.TRUE);
    }

    @ApiOperation(value = "用户注册")
    @PostMapping(value = "register")
    public ResponseResultVO register(@Valid UserRegisterInfoForm registerInfoVO) {
        User user = User.builder().build();
        BeanUtils.copyProperties(registerInfoVO, user);
        return ResponseResultUtil.success(userService.addOne(user));
    }

    @ApiOperation(value = "获取指定id用户信息")
    @GetMapping(value = "getUser/{id}")
    public ResponseResultVO getUserById(@Validated @PathVariable("id") String id) {
        return ResponseResultUtil.success(userService.queryOneById(id));
    }

    @ApiOperation(value = "获取指定用户名信息")
    @GetMapping(value = "getUser/{name}")
    public ResponseResultVO getUserByName(@PathVariable("name") String name) {
        return ResponseResultUtil.success(userService.queryOneByName(name));
    }
}
