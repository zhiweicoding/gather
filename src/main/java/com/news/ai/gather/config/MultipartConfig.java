package com.news.ai.gather.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;


/**
 * @Created by zhiwei on 2022/3/23.
 */
@Configurable
public class MultipartConfig {

    /**
     * 上传文件限制
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("2048000KB"));
        factory.setMaxRequestSize(DataSize.parse("2048000KB"));
        return factory.createMultipartConfig();
    }

}