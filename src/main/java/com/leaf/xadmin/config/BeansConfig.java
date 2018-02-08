package com.leaf.xadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author leaf
 * <p>date: 2018-02-09 0:30</p>
 * <p>version: 1.0</p>
 */
@Configuration
public class BeansConfig {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
