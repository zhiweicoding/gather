package com.news.ai.gather.services;


import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.bean.dto.VideoDto;
import com.news.ai.gather.bean.model.MsgBean;
import com.news.ai.gather.bean.model.VideoBean;

import java.util.List;
import java.util.Map;

/**
 * @author zhiwei
 */
public interface TwitterService {
    List<TwitterDto> splitRealInfo(Map<String, Object> params);

    List<MsgBean> packageTwitterData(String kolId, List<TwitterDto> dtoArray);

    String insertKol(TwitterDto.UserDto userDto);

    VideoBean insertVideo(VideoDto videoDto);
}
