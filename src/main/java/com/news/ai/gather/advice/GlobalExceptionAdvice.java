package com.news.ai.gather.advice;

import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.support.ResponseFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zhiwei
 * @date 5/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public BaseResponse<String> handlerCommerceException(HttpServletRequest req, Exception ex) {

        BaseResponse<String> response = ResponseFactory.fail(ex.getMessage());
        log.error("GlobalExceptionAdvice service has error: [{}]", ex.getMessage(), ex);
        return response;
    }
}
