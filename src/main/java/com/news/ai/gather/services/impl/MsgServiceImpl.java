package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.MsgBean;
import com.news.ai.gather.dao.MsgDao;
import com.news.ai.gather.services.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 */
@Slf4j
@Service(value = "msgService")
public class MsgServiceImpl extends ServiceImpl<MsgDao, MsgBean> implements MsgService {


}




