package com.zjun122;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjun122.mapper")
public class Zjun122AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(Zjun122AdminApplication.class, args);
    }
}
