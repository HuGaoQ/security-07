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

    /**
     * 自动检测用户是否被锁定
     */
    private String Automatically;

}