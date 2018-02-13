package com.leaf.xadmin.shiro.realm;

import com.leaf.xadmin.entity.Permission;
import com.leaf.xadmin.entity.Role;
import com.leaf.xadmin.entity.User;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.enums.UserStatus;
import com.leaf.xadmin.exception.GlobalException;
import com.leaf.xadmin.service.IPermissionService;
import com.leaf.xadmin.service.IRoleService;
import com.leaf.xadmin.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.List;

/**
 * 自定义用户权限域 UserRealm
 *
 * @author leaf
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    @Lazy
    private IRoleService roleService;
    @Autowired
    @Lazy
    private IPermissionService permissionService;
    @Autowired
    @Lazy
    private IUserService userService;

    /**
     * 授权
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取登录用户名
        String name = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取用户权限和角色列表
        List<Role> roles = roleService.queryUserRoles(name);
        List<Permission> permissions = permissionService.queryUserPermissions(name);

        // 加载用户角色列表
        for (Role role : roles) {
            authorizationInfo.addRole(role.getName());
        }

        // 加载用户权限列表
        for (Permission permission : permissions) {
            authorizationInfo.addStringPermission(permission.getName());
        }

        return authorizationInfo;

    }

    /**
     * 用户认证
     *
     * @param authenticationToken
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        // 获取登录用户名
        String name = (String) authenticationToken.getPrincipal();
        // 查找用户信息
        User user = userService.queryOneByName(name);
        if (user != null) {
            // 判断用户帐户是否被锁定
            if (!user.getStatus().equals(UserStatus.NORMAL.getCode())) {
                throw new GlobalException(ErrorStatus.ACCOUNT_LOCK_ERROR);
            }
            // 封装用户认证信息
            AuthenticationInfo authInfo = new SimpleAuthenticationInfo(user.getName(),
                    user.getPass(), getName());
            return authInfo;
        }

        return null;
    }

    /**
     * 清理全部权限缓存信息
     *
     */
    public void clearAllCachedAuthors() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
