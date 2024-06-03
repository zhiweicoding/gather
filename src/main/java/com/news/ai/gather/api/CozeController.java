package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * coze
 *
 * @Created by zhiwei on 2024/5/15.
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/api/coze")
public class CozeController {

    @Autowired
    @Qualifier(value = "emailService")
    private EmailService emailService;

    @Value("${email.defaultTo}")
    private String defaultTo;

    @PostMapping("/receive/page")
    public
    @ResponseBody
    BaseResponse<String> receivePage(@RequestBody Map<String, String> param) {
        log.info("receive page ,入参 : {}", JSON.toJSONString(param));
        try {
            String output = param.get("out_put");
            String title = param.get("title");
            emailService.sendNormal(defaultTo, title, output);
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("receive page ,入参：{}", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
