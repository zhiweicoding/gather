package com.news.ai.gather.advice;

import com.news.ai.gather.annotation.IgnoreResponseAnnotation;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author zhiweicoding.xyz
 * @date 5/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@RestControllerAdvice("com.news.ai.gather.api")
public class CommonResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        if (methodParameter.getMethod() == null) {
            return false;
        }
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAnnotation.class)) {
            return false;
        }
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAnnotation.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // 定义最终的返回对象
        BaseResponse<Object> response = ResponseFactory.get(ResponseFactory.StatsEnum.SUCCESS, o);
        if (null == o) {
            return ResponseFactory.get(ResponseFactory.StatsEnum.SUCCESS, null);
        } else if (o instanceof BaseResponse) {
            response = (BaseResponse<Object>) o;
        }
        return response;
    }

}
