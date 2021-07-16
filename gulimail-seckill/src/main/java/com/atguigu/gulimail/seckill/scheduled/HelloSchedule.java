package com.atguigu.gulimail.seckill.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 开启定时任务
 * @author shkstart
 * @create 2021-07-03 21:29
 */
@Slf4j
@Component

public class HelloSchedule {
    /**
     * 1、spring中6位组成，不允许第七位的年份
     */
    @Async
    @Scheduled(cron = "* * * * * ？")
    public void hello(){
        log.info("hello");
    }
}
