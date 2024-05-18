package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.QueryUserBean;
import com.news.ai.gather.dao.QueryUserDao;
import com.news.ai.gather.services.QueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 */
@Service(value = "queryService")
@Slf4j
public class QueryServiceImpl extends ServiceImpl<QueryUserDao, QueryUserBean> implements QueryService {


}




