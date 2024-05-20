package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.bean.vo.CookieVo;
import com.news.ai.gather.bean.vo.EmailMsgVo;
import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.services.ConfigService;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * transfer some variables
 *
 * @Created by zhiwei on 2024/5/15.
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/api/email")
public class EmailController {

    @Autowired
    @Qualifier(value = "emailService")
    private EmailService emailService;

    /**
     * set cookie
     *
     */
    @PostMapping("/single")
    public
    @ResponseBody
    BaseResponse<Boolean> setCookie(@RequestBody EmailMsgVo emailMsgVo) {
        log.info("email single ,入参 : {}", JSON.toJSONString(emailMsgVo));
        boolean result = emailService.sendOverdue(
                emailMsgVo.getTo(),
                emailMsgVo.getTitle(),
                emailMsgVo.getContent()
        );
        return ResponseFactory.success(result);
    }


}
