package com.ncamc.schedule;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: hugaoqiang
 * @CreateTime: 2022-08-04 11:13
 */
@Slf4j
public class ScheduleTask implements Runnable{
    @Override
    public void run() {
        System.out.println("我是一个定时任务");
    }
}