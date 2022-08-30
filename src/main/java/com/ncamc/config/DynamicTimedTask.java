package com.ncamc.config;

import cn.hutool.core.lang.UUID;
import com.ncamc.schedule.Automatically;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/***
 *  功能描述：定时任务调度类
 */
@Slf4j
@Component
@DependsOn("applicationContextProvider")
public class DynamicTimedTask {

    /**
     * 创建map存放线程
     */
    public static ConcurrentHashMap<String, ScheduledFuture> map = new ConcurrentHashMap<>();

    @Resource
    private Config config;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    // 线程池任务调度类,自定义的ThreadPoolTaskScheduler
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        executor.setPoolSize(20);
        return executor;
    }

    @PostConstruct
    public void init() {
        log.info("**************定时任务初始化开始**************");
        Automatically();
        log.info("**************自动检测用户是否被锁定初始化完毕**************");
    }

    //    @Scheduled(cron = "0/10 * * * * ?")
    public void Automatically() {
        ScheduledFuture scheduledFuture = threadPoolTaskScheduler.schedule(new Automatically(), new CronTrigger(config.getAutomatically()));
        map.put(UUID.randomUUID().toString(Boolean.TRUE), scheduledFuture);
    }

}