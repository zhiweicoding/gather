package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.GoodBean;
import com.news.ai.gather.dao.GoodDao;
import com.news.ai.gather.services.GoodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 * @description 针对表【t_good】的数据库操作Service实现
 * @createDate 2022-03-20 15:41:26
 */
@Service(value = "goodService")
@Slf4j
public class GoodServiceImpl extends ServiceImpl<GoodDao, GoodBean> implements GoodService {


}




