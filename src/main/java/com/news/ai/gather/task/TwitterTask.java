package com.news.ai.gather.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.dto.CollectParams;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.bean.model.QueryUserBean;
import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.services.QueryService;
import com.news.ai.gather.services.TwitterService;
import com.news.ai.gather.support.RedisSupport;
import com.news.ai.gather.support.TwitterCollect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * twitter
 *
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Component
public class TwitterTask {

    @Autowired
    private RedisSupport redisSupport;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private QueryService queryService;

    /**
     * each task 60 min execute once
     */
    @Scheduled(fixedDelay = 2L * 60L * 60L * 1000L)
    public void checkCacheIsOverdue() {
        if (redisSupport.exists(TwitterKeyConstants.TWITTER_COOKIE) && redisSupport.exists(TwitterKeyConstants.TWITTER_TOKEN)) {
            String twitterCookie = String.valueOf(redisSupport.get(TwitterKeyConstants.TWITTER_COOKIE));
            String twitterToken = String.valueOf(redisSupport.get(TwitterKeyConstants.TWITTER_TOKEN));
            log.info("twitter cookie : {} , twitter token : {}", twitterCookie, twitterToken);
            List<QueryUserBean> userList = queryService.list(Wrappers.<QueryUserBean>lambdaQuery()
                    .eq(QueryUserBean::getIsDelete, 1)
                    .eq(QueryUserBean::getQType, "twitter")
                    .select(QueryUserBean::getUserId, QueryUserBean::getUserAccount));
            for (QueryUserBean bean : userList) {
                try {
                    TwitterCollect collect = new TwitterCollect();
                    CollectParams<TwitterService> param = new CollectParams<>();
                    param.setUrl("https://x.com/i/api/graphql/gQlOy4mD5C8M8fYxqa0FJg/UserTweets");
                    param.setService(twitterService);
                    param.setTxt(bean.getUserAccount());
                    param.setKey(bean.getUserId());
                    param.setCookie(twitterCookie);
                    param.setToken(twitterToken);
                    List<TwitterDto> insertColl = collect.collect(param);
                    log.info("twitter collect result : {}", insertColl.size());
                    Random r = new Random(10000);
                    int randomSleep = r.nextInt();
                    try {
                        Thread.sleep(randomSleep);
                    } catch (InterruptedException e) {
                        log.error("sleep error,{}", e.getMessage(), e);
                    }
                } catch (Exception e) {
                    log.error("twitter collect error : {},[{}]", e.getMessage(), bean.getUserAccount(), e);
                }
            }
        } else {
            log.error("twitter cookie or twitter token is null");
        }
    }


}
