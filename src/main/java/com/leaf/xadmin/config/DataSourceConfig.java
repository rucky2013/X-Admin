package com.leaf.xadmin.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据源配置
 *
 * @author leaf
 * <p>date: 2017-12-30 4:59</p>
 */
@Configuration
@ConfigurationProperties(prefix = "druid")
@PropertySource("classpath:druid/druid.properties")
public class DataSourceConfig {
    @Setter @Getter private String allow;
    @Setter @Getter private String deny;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private String reset;

    /**
     * 数据源参数配置
     *
     * @param environment
     * @return
     */
    @Bean
    public DataSource dataSource(Environment environment) {
        return DruidDataSourceBuilder
                .create()
                .build(environment, "spring.datasource.druid.");
    }

    /**
     * 访问地址: http://localhost:8080/xadmin/druid/index.html
     *
     * 注册ServletRegistrationBean
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean registrationBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        // IP白名单
        bean.addInitParameter("allow", allow);
        // IP黑名单(若同时满足时，deny优先于allow; 若仅满足deny，提示"Sorry, you are not permitted to view this page.")
        bean.addInitParameter("deny", deny);
        // 登录账号密码
        bean.addInitParameter("loginUsername", username);
        bean.addInitParameter("loginPassword", password);
        // 是否能够重置数据
        bean.addInitParameter("resetEnable", reset);
        return bean;
    }

    /**
     * 注册FilterRegistrationBean
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new WebStatFilter());
        // 添加过滤规则
        bean.addUrlPatterns("/*");
        // 添加不需要忽略的格式信息
        bean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }
}
