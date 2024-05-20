package com.news.ai.gather.services;


import com.baomidou.mybatisplus.extension.service.IService;
import com.news.ai.gather.bean.model.ConfigBean;
import com.news.ai.gather.bean.model.EmailBean;

import java.util.List;

/**
 * @author zhiwei
 */
public interface EmailService extends IService<EmailBean> {

    boolean sendOverdue(String to, String title, String content);

    void sendOverdue(List<String> to, String title, String content);
}
