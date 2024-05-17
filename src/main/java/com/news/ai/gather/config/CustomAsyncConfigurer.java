package com.news.ai.gather.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

/**
 * all async need use this config
 * @author zhiweicoding.xyz
 * @date 5/18/24
 * @email diaozhiwei2k@gmail.com
 */
@Configuration
@Slf4j
@EnableAsync
public class CustomAsyncConfigurer implements AsyncConfigurer {

    private final Executor gatherExecutor;

    public CustomAsyncConfigurer(Executor gatherExecutor) {
        this.gatherExecutor = gatherExecutor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return gatherExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, obj) -> {
            log.error("Method name - {}", method.getName());
            log.error("Parameter values - {}", obj);
            log.error("Exception in async method: {}", throwable.getMessage(), throwable);
        };
    }
}
