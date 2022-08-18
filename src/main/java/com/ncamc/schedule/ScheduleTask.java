package com.ncamc.schedule;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试启动定时任务
 */
@Slf4j
public class ScheduleTask implements Runnable {
    @Override
    public void run() {
        System.out.println("我是一个定时任务");
    }
}