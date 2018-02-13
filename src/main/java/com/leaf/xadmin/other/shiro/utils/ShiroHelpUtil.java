package com.leaf.xadmin.other.shiro.util;

import com.google.common.base.Strings;
import com.leaf.xadmin.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author leaf
 * <p>date: 2018-01-10 21:08</p>
 */
@Component
@Slf4j
public class ShiroHelpUtil {

    @Autowired
    private RedisSessionDAO sessionDAO;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 移除指定用户缓存权限信息
     *
     * @param name
     * @return
     */
    public boolean removeAuthorsByName(String name) {
        // 判断是否已经存在
        if (redisTemplate.opsForSet().isMember(GlobalConstants.SESSION_FLUSH_AUTHOR_KEY, name)) {
            return true;
        }
        return redisTemplate.opsForSet().add(GlobalConstants.SESSION_FLUSH_AUTHOR_KEY, name) == 1;
    }

    /**
     * 获取已登录用户session列表
     *
     * @return
     */
    public Collection<Session> getActiveSessions() {
        return sessionDAO.getActiveSessions();
    }

    /**
     * 踢出指定登录用户
     *
     * @param name
     * @return
     */
    public boolean removeLoginSessionByName(String name) {
        Session session = getLoginSession(name);
        if (session != null) {
            session.setAttribute(
                    GlobalConstants.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);
            sessionDAO.update(session);
        }

        return true;
    }

    /**
     * 获取登录用户session信息
     *
     * @param name
     * @return
     */
    public Session getLoginSession(String name) {
        if (!Strings.isNullOrEmpty(name)) {
            // 获取当前系统存活session
            Collection<Session> sessions = sessionDAO.getActiveSessions();

            for (Session session : sessions) {
                if (name.equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    return session;
                }
            }
        }

        return null;
    }
}
