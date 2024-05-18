package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.dto.TwitterDto;
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
import java.util.stream.Collectors;

/**
 * gather user info
 *
 * @Created by zhiwei on 2024/5/15.
 */
@RestController
@RequestMapping(value = "/v1/api/user")
@Slf4j
public class QueryController {

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
    @GetMapping("/query/{type}/{args}")
    public
    @ResponseBody
    BaseResponse<List<String>> queryUserList(@PathVariable("type") String type,
                                             @PathVariable("args") String args) {
        log.debug("query user domain ,入参 : {},args:{}", type, args);
        try {
            if (type == null || args == null || type.isEmpty() || args.isEmpty()) {
                return ResponseFactory.argsNotCorrect(null);
            }
            LambdaQueryWrapper<QueryUserBean> wrapper = Wrappers.<QueryUserBean>lambdaQuery()
                    .eq(QueryUserBean::getIsDelete, 1);
            if (!"all".equals(type)) {
                wrapper.eq(QueryUserBean::getQType, type);
            }
            if (!"all".equals(args)) {
                wrapper.eq(QueryUserBean::getQUserId, args);
            }
            List<QueryUserBean> resultList = queryService.list(wrapper);
            return ResponseFactory.success(
                    resultList.stream()
                            .map(q -> {
                                String qUserId = q.getQUserId();
                                return twitterUserList.replace("{userId}", qUserId);
                            })
                            .collect(Collectors.toList())
            );
        } catch (Exception e) {
            log.error("query user domain error：{}", e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }
}
