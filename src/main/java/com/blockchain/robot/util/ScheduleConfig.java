package com.blockchain.robot.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        //多线程并发定时任务，不设置的话，所有的定时在单线程
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(10));
    }
}