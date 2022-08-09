package com.ncamc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-07-05 10:17
 */
@EnableSwagger2
@SpringBootApplication
@MapperScan("com.ncamc.mapper")
@EnableScheduling
public class SecurityApplication07 {
    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication07.class, args);
    }
}