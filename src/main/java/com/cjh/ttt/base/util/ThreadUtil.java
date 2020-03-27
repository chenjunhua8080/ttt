package com.cjh.ttt.base.util;

import io.netty.util.concurrent.DefaultThreadFactory;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池工具类
 *
 * @author cjh
 * @date 2020/3/26
 */
@Slf4j
public class ThreadUtil {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPoolExecutor = new ThreadPoolExecutor(
            4,
            4,
            0,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(1000),
            new DefaultThreadFactory("通用线程池")
        );
        log.info("初始化线程池: {}", threadPoolExecutor);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
