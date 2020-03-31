package com.cjh.ttt.job;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * test job
 *
 * @author cjh
 * @date 2020/3/31
 */
@RefreshScope
@Component
@EnableScheduling
@Slf4j
public class TestJob {


    @Value("${job.test.enable}")
    private Boolean testEnable;

    @Scheduled(cron = "${job.test.cron}")
    public void test() {
        if (testEnable) {
            System.out.println("test job: " + new Date());
        }
    }

}
