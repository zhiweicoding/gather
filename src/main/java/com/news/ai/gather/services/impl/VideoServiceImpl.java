package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.ConfigBean;
import com.news.ai.gather.bean.model.VideoBean;
import com.news.ai.gather.dao.ConfigDao;
import com.news.ai.gather.dao.VideoDao;
import com.news.ai.gather.services.ConfigService;
import com.news.ai.gather.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhiwei
 */
@Slf4j
@Service(value = "videoService")
public class VideoServiceImpl extends ServiceImpl<VideoDao, VideoBean> implements VideoService {


}




