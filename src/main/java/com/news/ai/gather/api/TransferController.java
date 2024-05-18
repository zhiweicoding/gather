package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.bean.model.QueryUserBean;
import com.news.ai.gather.services.QueryService;
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
 * gather user info
 *
 * @Created by zhiwei on 2024/5/15.
 */
@RestController
@RequestMapping(value = "/v1/api/transfer")
@Slf4j
public class TransferController {

    @Autowired
    @Qualifier(value = "queryService")
    private QueryService queryService;

    @Value("${twitter.userLink}")
    private String twitterUserList;

    /**
     * query user domain
     *
     * @return
     */
    @PostMapping("/set/cookie")
    public
    @ResponseBody
    BaseResponse<List<String>> setCookie(@RequestBody Object body) {
        log.info("set cookie ,入参 : {}", JSON.toJSONString(body));
        return ResponseFactory.success(null);
    }
}
