package com.news.ai.gather.task;

import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.support.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * redis cache overdue task
 *
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Component
public class CacheOverdueTask {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisSupport redisSupport;

    @Value("${email.defaultTo}")
    private String defaultTo;

    private static final long min_expire_time = 60L * 30L;
    private static final long min_health_check_expire_time = 30L;

    /**
     * checkCacheIsOverdue
     * each 50 min execute once
     */
    @Scheduled(fixedDelay = 3000L * 1000L)
    public void checkCacheIsOverdue() {
        log.info("start task job ! check cache is overdue");
        long cookie = redisSupport.expireTime(TwitterKeyConstants.TWITTER_COOKIE);
        long token = redisSupport.expireTime(TwitterKeyConstants.TWITTER_TOKEN);
        if (cookie < min_expire_time) {
            log.info("cookie is overdue , send email");
            emailService.sendOverdue(defaultTo, "twitter cookie is overdue", "twitter cookie is overdue,please check Automa");
        }
        if (token < min_expire_time) {
            log.info("token is overdue , send email");
            emailService.sendOverdue(defaultTo, "twitter token is overdue", "twitter token is overdue,please check Automa");

        }
        log.info("end task job ! check cache is overdue ");
    }

    @Scheduled(fixedDelay = 60L * 1000L)
    public void healthCheck() {
        log.info("start healthCheck ! check cache is overdue");
        long healthCheck = redisSupport.expireTime(TwitterKeyConstants.TWITTER_HEALTH_CHECK);
        if (healthCheck < min_health_check_expire_time) {
            log.info("healthCheck is overdue , send email");
            emailService.sendOverdue(defaultTo, "health check result is fail ", "please check automa !!!! ");
        }
        log.info("end healthCheck ! check cache is overdue ");
    }


}
