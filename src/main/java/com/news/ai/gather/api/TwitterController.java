package com.news.ai.gather.api;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.entity.BaseResponse;
import com.news.ai.gather.bean.model.GoodBean;
import com.news.ai.gather.bean.vo.IndexVo;
import com.news.ai.gather.services.GoodService;
import com.news.ai.gather.support.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * gather twitter info
 *
 * @Created by zhiwei on 2024/5/15.
 */
@RestController
@RequestMapping(value = "/v1/api/twitter")
@Slf4j
public class TwitterController {

    @Autowired
    @Qualifier(value = "goodService")
    private GoodService goodService;

    /**
     * 根据user获取用户的twitter
     *
     * @param param {@link IndexVo}
     * @return
     */
//    @Cacheable(value = "60s", keyGenerator = "cacheJsonKeyGenerator", condition = "#param != null", unless = "#result == null || #result.getIsEmpty()")
    @PostMapping("/receive/user/list")
    public
    @ResponseBody
    BaseResponse<String> receiveUserList(@RequestBody Map<String, Object> param) {
        log.debug("根据user获取用户的twitter,入参 : {}", JSON.toJSONString(param));
        try {
            log.info("param:{}", JSON.toJSONString(param));
            log.debug("根据user获取用户的twitter success");
            return ResponseFactory.success("根据user获取用户的twitter success");
        } catch (Exception e) {
            log.error("根据user获取用户的twitter error：" + e.getMessage(), e);
            return ResponseFactory.fail(null);
        }
    }


}
