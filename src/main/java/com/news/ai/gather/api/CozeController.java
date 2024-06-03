package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.bean.vo.EmailMsgVo;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @PostMapping("/receive/page")
    public
    @ResponseBody
    BaseResponse<String> receivePage(@RequestBody Object param) {
        log.debug("receive page ,入参 : {}", JSON.toJSONString(param));
        try {
            log.info("param:{}", JSON.toJSONString(param));
            return ResponseFactory.success(null);
        } catch (Exception e) {
            log.error("receive page ,入参：{}", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
