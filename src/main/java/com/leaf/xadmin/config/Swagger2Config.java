package com.leaf.xadmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author leaf
 * <p>date: 2018-01-02 19:46</p>
 */
@Configuration
@EnableSwagger2
@Profile({ "dev", "test" })
public class Swagger2Config {

    /**
     * 访问地址: http://localhost:8080/rent/swagger-ui.html
     *
     * @return
     */

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.leaf.xadmin.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot + swagger项目api文档")
                .description("一款基于swagger2打造的在线api文档")
                .version("v1.0")
                .build();
    }
}
