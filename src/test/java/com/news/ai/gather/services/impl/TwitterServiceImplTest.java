package com.news.ai.gather.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.news.ai.gather.bean.dto.TwitterDto;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
class TwitterServiceImplTest {


    @Test
    void splitRealInfo() {
        Map<String, Object> parse = JSON.parseObject("1", new TypeReference<>() {
        });
        TwitterServiceImpl twitterService = new TwitterServiceImpl();
        List<TwitterDto> twitterDtos = twitterService.splitRealInfo(parse);
        for (TwitterDto twitterDto : twitterDtos) {
            System.out.println(JSON.toJSONString(twitterDto));
        }
    }
}