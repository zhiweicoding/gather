package com.news.ai.gather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhiweicoding.xyz
 * @date 5/18/24
 * @email diaozhiwei2k@gmail.com
 */
@Configuration
public class ExecutorConfig {

    @Value("${threadpool.corePoolSize}")
    private int corePoolSize;
    @Value("${threadpool.maxPoolSize}")
    private int maxPoolSize;
    @Value("${threadpool.queueCapacity}")
    private int queueCapacity;
    @Value("${threadpool.keepAliveTime}")
    private int keepAliveTime;
    @Value("${threadpool.threadNamePrefix}")
    private String threadNamePrefix;

    @Bean(name = "gatherExecutor")
    public Executor gatherExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize); // 核心线程池数
        executor.setMaxPoolSize(maxPoolSize); // 最大线程数
        executor.setQueueCapacity(queueCapacity); // 队列容量
        executor.setKeepAliveSeconds(keepAliveTime); // 空闲线程存活时间
        executor.setThreadNamePrefix(threadNamePrefix); // 线程名称前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        // 执行初始化
        executor.initialize();
        return executor;
    }

    @Bean
    public CustomAsyncConfigurer customAsyncConfigurer() {
        return new CustomAsyncConfigurer(gatherExecutor());
    }
}
