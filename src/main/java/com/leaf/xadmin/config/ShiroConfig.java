package com.leaf.xadmin.config;

import com.leaf.xadmin.constants.GlobalConstants;
import com.leaf.xadmin.shiro.filter.AuthorFlushFilter;
import com.leaf.xadmin.shiro.filter.ForceLogoutFilter;
import com.leaf.xadmin.shiro.filter.TokenValidFilter;
import com.leaf.xadmin.shiro.matcher.PasswordMatcher;
import com.leaf.xadmin.shiro.realm.MyShiroRealm;
import com.leaf.xadmin.shiro.redis.JwtSessionIdGenerator;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author leaf
 * <p>date: 2018-01-02 19:56</p>
 */
@Configuration
@PropertySource(value = {"classpath:application.yml", "classpath:shiro/shiro.properties"})
public class ShiroConfig {

    @Value("${shiro.secret.key}")
    @Setter @Getter private String secretKey;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Autowired
    private PasswordMatcher passwordMatcher;

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor
                = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 添加过滤器
        Map<String, Filter> filters = new HashMap<>(10);
        filters.put("tokenValid", new TokenValidFilter());
        filters.put("forceLogout", new ForceLogoutFilter());
        filters.put("authorFlush", new AuthorFlushFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 拦截url，并添加响应过滤器
        Map<String, String> filterChainDefinitionManager = new LinkedHashMap<>();
        filterChainDefinitionManager.put("/user/login", "anon");
        filterChainDefinitionManager.put("/user/**", "tokenValid,forceLogout,authorFlush");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(myShiroRealm());
        // redis自定义缓存实现
        securityManager.setCacheManager(redisCacheManager());
        // redis自定义session管理
        securityManager.setSessionManager(redisSessionManager());
        return securityManager;
    }

    /**
     * 身份认证realm
     *
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm() {
        MyShiroRealm shiroRealm = new MyShiroRealm();
        shiroRealm.setCredentialsMatcher(passwordMatcher);
        return shiroRealm;
    }
    /**
     * 配置shiro redisManager
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        // 配置缓存过期时间
        redisManager.setExpire(1800);
        redisManager.setTimeout(timeout);

        return redisManager;
    }

    /**
     * cacheManager 缓存
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO
     *
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setSessionIdGenerator(new JwtSessionIdGenerator());
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setKeyPrefix(GlobalConstants.REDIS_SESSION_KEY_PREFIX);
        return redisSessionDAO;
    }

    /**
     * SessionManager
     *
     * @return
     */
    @Bean
    public DefaultSessionManager redisSessionManager() {
        DefaultSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setGlobalSessionTimeout(GlobalConstants.JWT_TOKEN_TIMEOUT * 60 * 1000);
        sessionManager.setSessionValidationInterval(600000);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

}
