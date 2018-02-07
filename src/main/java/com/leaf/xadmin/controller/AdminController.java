package com.leaf.xadmin.controller;

import com.leaf.xadmin.entity.Resource;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.enums.LoginType;
import com.leaf.xadmin.exception.RepeatLoginException;
import com.leaf.xadmin.service.IAdminService;
import com.leaf.xadmin.service.IResourceService;
import com.leaf.xadmin.shiro.token.ExtendedUsernamePasswordToken;
import com.leaf.xadmin.utils.request.RequestResolveUtil;
import com.leaf.xadmin.utils.response.ResponseResultUtil;
import com.leaf.xadmin.vo.RequestResourceVO;
import com.leaf.xadmin.vo.ResponseResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-01-21 15:57</p>
 * <p>version: 1.0</p>
 */
@Api(value = "管理员相关请求", description = "/admin")
@RestController
@RequestMapping("admin")
@CrossOrigin
@Slf4j
public class AdminController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private RequestResolveUtil requestResolveUtil;

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IResourceService resourceService;

    @ApiOperation(value = "管理员登录")
    @PostMapping(value = "login")
    public ResponseResultVO login(@RequestParam("name") String name, @RequestParam("pass") String pass) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(name, pass, LoginType.ADMIN.getType());
            subject.login(token);
            return ResponseResultUtil.success(true);
        } else {
            // 退出，重新登录
            logout();
            return login(name, pass);
        }
    }

    @ApiOperation(value = "用户退出")
    @PostMapping(value = "logout")
    public ResponseResultVO logout() {
        // 清除用户权限缓存
        SecurityUtils.getSubject().logout();
        return ResponseResultUtil.success(true);
    }

    @ApiOperation(value = "获取在线登陆会话信息")
    @PostMapping(value = "getOnline")
    public ResponseResultVO getOnline() {
        return ResponseResultUtil.success(adminService.queryActiveSessions());
    }

    @ApiOperation(value = "强制指定用户下线")
    @PostMapping(value = "forceLogoutByName")
    public ResponseResultVO forceLogoutByName(@RequestParam("name") String name) {
        return ResponseResultUtil.success(adminService.forceLogoutByName(name));
    }

    @ApiOperation(value = "清理指定用户权限缓存")
    @PostMapping(value = "clearAuthorsByName")
    public ResponseResultVO clearAuthorsByName(@RequestParam("name") String name) {
        return ResponseResultUtil.success(adminService.clearAuthorsByName(name));
    }

    @ApiOperation(value = "持久化资源列表")
    @PostMapping(value = "scanAllResources")
    public ResponseResultVO scanAllResources() {
        List<Resource> resourceList = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            List<RequestResourceVO> requestResourceVOS = requestResolveUtil.methodResolver(method.getMethod());
            Resource resource = RequestResolveUtil.pathMergeUpgrade(requestResourceVOS);
            resourceList.add(resource);
        }

        return ResponseResultUtil.success(resourceService.addOrUpdateBatch(resourceList));
    }
}
