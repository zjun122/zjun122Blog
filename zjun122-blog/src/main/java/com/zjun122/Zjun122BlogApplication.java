package com.zjun122;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.zjun122.mapper")
@EnableScheduling
@EnableSwagger2
public class Zjun122BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(Zjun122BlogApplication.class, args);
    }
}
