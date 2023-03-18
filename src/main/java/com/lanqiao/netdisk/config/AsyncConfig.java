package com.lanqiao.netdisk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @description: 在Spring Boot应用程序初始化阶段实例化线程池
 * keepAliveSeconds默认为60，无需修改。
 * allowCoreThreadTimeOut默认为false，核心线程不会自动关闭
 * @author: BAISHUN
 * @date: 2023/3/11
 * @Copyright: 博客：https://www.cnblogs.com/baishun666/
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    /** 核心线程数 */
    static final int CORE_POOL_SIZE = 5;
    /** 最大线程数 */
    static final int MAX_POOL_SIZE = 11;

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
