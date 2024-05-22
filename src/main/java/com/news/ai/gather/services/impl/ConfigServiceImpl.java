package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.ConfigBean;
import com.news.ai.gather.bean.model.QueryUserBean;
import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.dao.ConfigDao;
import com.news.ai.gather.dao.QueryUserDao;
import com.news.ai.gather.services.ConfigService;
import com.news.ai.gather.services.QueryService;
import com.news.ai.gather.support.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * @author zhiwei
 */
@Slf4j
@Service(value = "configService")
public class ConfigServiceImpl extends ServiceImpl<ConfigDao, ConfigBean> implements ConfigService {

    @Autowired
    private RedisSupport redisSupport;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean refreshConfig(String key, String value) {
        try {
            boolean existCookie = this.exists(Wrappers.<ConfigBean>lambdaQuery()
                    .eq(ConfigBean::getConfigName, key)
                    .eq(ConfigBean::getIsDelete, 1)
            );
            if (existCookie) {
                this.update(null, Wrappers.<ConfigBean>lambdaUpdate()
                        .eq(ConfigBean::getConfigName, key)
                        .set(ConfigBean::getConfigValue, value)
                );
            } else {
                ConfigBean configBean = new ConfigBean();
                configBean.setConfigName(key);
                configBean.setConfigValue(value);
                configBean.setIsDelete(1);
                this.save(configBean);
            }
            redisSupport.set(key, value, 8L, TimeUnit.HOURS);
            return true;
        } catch (Exception e) {
            log.error("refreshConfig error,{}", e.getMessage(), e);
            return false;
        }

    }

    @Override
    public void healthCheck(String random) {
        redisSupport.set(TwitterKeyConstants.TWITTER_HEALTH_CHECK, random, 5L, TimeUnit.MINUTES);
    }
}




