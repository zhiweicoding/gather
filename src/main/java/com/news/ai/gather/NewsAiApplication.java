package com.news.ai.gather;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.news.ai.gather.dao")
@EnableConfigurationProperties
@EnableAspectJAutoProxy
public class NewsAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsAiApplication.class, args);
        System.out.println("launch success!");
    }

}
