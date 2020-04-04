package com.cjh.ttt.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * test job
 *
 * @author cjh
 * @date 2020/3/31
 */
@Component
@EnableScheduling
@Slf4j
public class TestJob {

    @Scheduled(cron = "${job.test}")
    public void test() {
        log.info("[---- test job ----]");
    }

}
