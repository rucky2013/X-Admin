package com.leaf.xadmin.shiro.filter;

import com.leaf.xadmin.exception.GlobalException;
import com.leaf.xadmin.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 验证token的合法性
 *
 * @author leaf
 * <p>date: 2018-01-15 17:09</p>
 * <p>version: 1.0</p>
 */
@Slf4j
public class TokenValidFilter extends AdviceFilter {

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) {
        // 解析request中的token参数
        String token = JwtUtil.getTokenFromRequest((HttpServletRequest) request);
        JwtUtil.verifyToken(token);
        return true;
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {

    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) {
        if (exception instanceof GlobalException) {
            log.error(SecurityUtils.getSubject().getPrincipal().toString() + "用户token无效，已被强制下线...");
            SecurityUtils.getSubject().logout();
        }
    }
}
