package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.bean.model.ConfigBean;
import com.news.ai.gather.bean.model.QueryUserBean;
import com.news.ai.gather.bean.vo.CookieVo;
import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.dao.ConfigDao;
import com.news.ai.gather.services.ConfigService;
import com.news.ai.gather.services.QueryService;
import com.news.ai.gather.support.RedisSupport;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * transfer some variables
 *
 * @Created by zhiwei on 2024/5/15.
 */
@Slf4j
@RestController
@RequestMapping(value = "/v1/api/transfer")
public class TransferController {

    @Autowired
    @Qualifier(value = "configService")
    private ConfigService configService;

    /**
     * set cookie
     *
     * @return
     */
    @PostMapping("/set/cookie")
    public
    @ResponseBody
    BaseResponse<Boolean> setCookie(@RequestBody CookieVo cookieVo) {
        log.info("set cookie ,入参 : {}", JSON.toJSONString(cookieVo));
        boolean refreshResult = configService.refreshConfig(TwitterKeyConstants.TWITTER_COOKIE, cookieVo.getCookie());
        return ResponseFactory.success(refreshResult);
    }

    /**
     * set token
     * <p>
     * {
     * "content-type": "application/json",
     * "X-Client-UUID": "b902c109-e74f-43b1-9831-f61794a28e79",
     * "authorization": "Bearer AAAAAAAAAAAAAAAAAAAAANRILgAAAAAAnNwIzUejRCOuH5E6I8xnZz4puTs%3D1Zv7ttfk8LF81IUq16cHjhLTvJu4FA33AGWWjCpTnA",
     * "x-twitter-auth-type": "OAuth2Session",
     * "x-csrf-token": "d00ccb8d4151b9dd6353bca7f6fef18e5a1cb2a7449cc862e632eec0b23c143c7774b05fbd7e4e3a06b4f122d1cda55081675edc8abb6b73792227fecad356ce7d7360e3cd5bef97ad3cf579d64de112",
     * "x-twitter-client-language": "en",
     * "x-twitter-active-user": "yes",
     * "x-client-transaction-id": "KbGDVxB8uJG5AR89SoZhMDMT3vRHHgECT3vXReyM2f+pHBQ3oMZ3ZduDTdN2XViMq1uZ0yh9a6lnZq8A1hmWNFtbkpulKg"
     * }
     * </p>
     *
     * @return
     */
    @PostMapping("/set/token")
    public
    @ResponseBody
    BaseResponse<Boolean> setToken(@RequestBody Map<String, String> token) {
        log.info("set token ,入参 : {}", JSON.toJSONString(token));
        boolean refreshResult = configService.refreshConfig(TwitterKeyConstants.TWITTER_TOKEN, JSON.toJSONString(token));
        return ResponseFactory.success(refreshResult);
    }

}
