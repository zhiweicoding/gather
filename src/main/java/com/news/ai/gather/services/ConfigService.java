package com.news.ai.gather.services;


import com.baomidou.mybatisplus.extension.service.IService;
import com.news.ai.gather.bean.model.ConfigBean;
import com.news.ai.gather.bean.model.QueryUserBean;

/**
 * @author zhiwei
 */
public interface ConfigService extends IService<ConfigBean> {

    boolean refreshConfig(String key, String value);
}
