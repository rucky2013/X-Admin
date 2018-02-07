package com.leaf.xadmin.shiro.filter;

import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.exception.GlobalException;
import com.leaf.xadmin.shiro.realm.UserRealm;
import com.leaf.xadmin.utils.redis.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 刷新用户权限
 *
 * @author leaf
 * <p>date: 2018-01-28 15:58</p>
 * <p>version: 1.0</p>
 */
@Slf4j
public class AuthorFlushFilter extends AccessControlFilter {
    /**
     * 当返回true时继续向下执行，但返回false时需要执行onAccessDenied方法
     *
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        // 判断redis中是否缓存该用户
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal != null) {
            String name = principal.toString();
            return !JedisUtil.getSet(GlobalConstants.SESSION_FLUSH_AUTHOR_KEY).contains(name);
        }

        // 未登录异常
        throw new GlobalException(ErrorStatus.AUTHEN_LACK_ERROR);
    }

    /**
     * 返回true时继续向下执行，但返回false时中断执行
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 清空该用户的所有权限缓存信息
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm shiroRealm = (UserRealm) rsm.getRealms().iterator().next();
        shiroRealm.clearAllCachedAuthors();

        // 删除redis权限刷新标志
        String name = SecurityUtils.getSubject().getPrincipal().toString();
        return JedisUtil.removeSet(GlobalConstants.SESSION_FLUSH_AUTHOR_KEY, name) == 1;
    }
}
