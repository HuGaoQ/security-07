package com.ncamc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "scheduleinfo")
public class Config {

    //Test 定时任务
    private String scheduletask;

}