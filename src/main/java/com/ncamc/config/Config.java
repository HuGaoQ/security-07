package com.ncamc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-08-04 11:02
 */
@Data
@Component
@ConfigurationProperties(prefix = "scheduleinfo")
public class Config {

    private String scheduletask;

}