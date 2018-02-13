package com.leaf.xadmin.shiro.filter;

import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.enums.ErrorStatus;
import com.leaf.xadmin.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 强制用户退出
 *
 * @author leaf
 * <p>date: 2018-01-28 13:49</p>
 * <p>version: 1.0</p>
 */
@Slf4j
public class ForceLogoutFilter extends AccessControlFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        Session session = getSubject(request, response).getSession(false);
        if (session == null) {
            return true;
        }
        return session.getAttribute(GlobalConstants.SESSION_FORCE_LOGOUT_KEY) == null;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        log.debug(SecurityUtils.getSubject().getPrincipal().toString() + "用户已被管理员强制下线...");
        getSubject(request, response).logout();
        throw new GlobalException(ErrorStatus.FORCE_LOGOUT_ERROR);
    }
}
