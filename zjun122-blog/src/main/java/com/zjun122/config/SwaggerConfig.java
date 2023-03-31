package com.zjun122.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) //文档信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.zjun122.controller")) //包名路径
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("zjun122", "http://zjun122.com", "123@qq.com");
        return new ApiInfoBuilder()
                .title("博客前台文档")
                .description("前台文档")
                .contact(contact)   // 联系方式
                .version("1.1.1")  // 版本
                .build();
    }
}