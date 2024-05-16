package com.news.ai.gather.services;


import com.news.ai.gather.bean.dto.TwitterDto;

import java.util.List;
import java.util.Map;

/**
 * @author zhiwei
 */
public interface TwitterService {
    List<TwitterDto> splitRealInfo(Map<String, Object> params);
}
