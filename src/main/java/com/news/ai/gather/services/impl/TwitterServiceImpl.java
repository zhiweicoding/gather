package com.news.ai.gather.services.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.services.TwitterService;
import com.news.ai.gather.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhiwei
 */
@Service(value = "twitterService")
@Slf4j
public class TwitterServiceImpl implements TwitterService {


    @Override
    public void splitRealInfo(Map<String, Object> params) {
        Map<String, Object> data = (Map<String, Object>) params.get("data");
        Map<String, Object> thread = (Map<String, Object>) data.get("threaded_conversation_with_injections_v2");
        List<Map<String, Object>> instructions = (List<Map<String, Object>>) thread.get("instructions");
        List<String> entries = new ArrayList<>();
        for (Map<String, Object> instruction : instructions) {
            String type = String.valueOf(instruction.get("type"));
            if ("TimelineAddEntries".equals(type)) {
                List<Map<String, Object>> entriesArray = (List<Map<String, Object>>) instruction.get("entries");
                for (Map<String, Object> eItem : entriesArray) {
                    entries.add(JSON.toJSONString(eItem));
                }
            }
        }

        if (!entries.isEmpty()) {
            for (String item : entries) {
                JSONObject itemJson = JSON.parseObject(item);
                JSONObject content = itemJson.getJSONObject("content");
                if (content.containsKey("itemContent")) {
                    JSONObject itemContent = content.getJSONObject("itemContent");
                    String itemType = content.getString("itemType");
                    if ("TimelineTweet".equals(itemType)) {
                        JSONObject resultJson = itemContent.getJSONObject("tweet_results").getJSONObject("result");
                        parseItemContent(resultJson);
                    }
                } else if (content.containsKey("items")) {
                    JSONArray items = content.getJSONArray("items");
                    parseItems(items);
                }
            }
        }
    }

    private TwitterDto parseItemContent(JSONObject resultJson) {
        //tweet id
        String tweetId = resultJson.getString("rest_id");
        TwitterDto.UserDto userDto = parseUserDto(resultJson);
        JSONObject legacy = resultJson.getJSONObject("legacy");

        TwitterDto twitterDto = new TwitterDto();
        twitterDto.setTweetId(tweetId);
        twitterDto.setUserDto(userDto);
        //full text
        String fullText = legacy.getString("full_text");
        twitterDto.setFullText(fullText);
        //redirect_url
        String expanded = legacy.getJSONObject("quoted_status_permalink").getString("expanded");
        twitterDto.setExpanded(expanded);
        //msg created at
        long createdAt = DateUtil.conversionDate(legacy.getString("created_at"));
        twitterDto.setCreatedAt(createdAt);

        JSONArray mediaArray = legacy.getJSONObject("entities").getJSONArray("media");
        List<String> photoUrlArray = new ArrayList<>();
        List<String> videoUrlArray = new ArrayList<>();
        for (int i = 0; i < mediaArray.size(); i++) {
            JSONObject media = mediaArray.getJSONObject(i);
            String type = media.getString("type");
            String mediaUrl = media.getString("media_url_https");
            if ("photo".equals(type)) {
                photoUrlArray.add(mediaUrl);
            } else {
                videoUrlArray.add(mediaUrl);
            }

        }
        twitterDto.setPhotoUrlArray(photoUrlArray);
        twitterDto.setVideoUrlArray(videoUrlArray);

        //contains referenced tweets
        if (legacy.containsKey("quoted_status_result")) {
            JSONObject quotedStatusResult = legacy.getJSONObject("quoted_status_result").getJSONObject("result");
            TwitterDto subTwitterDto = parseItemContent(quotedStatusResult);
            twitterDto.setSubTwitterDto(subTwitterDto);
        }

        return twitterDto;
    }

    private void parseItems(JSONArray items) {

    }

    private TwitterDto.UserDto parseUserDto(JSONObject resultJson) {
        TwitterDto.UserDto userDto = new TwitterDto.UserDto();
        try {
            JSONObject userLegacy = resultJson.getJSONObject("core").getJSONObject("user_results").getJSONObject("result").getJSONObject("legacy");
            userDto.setDescription(userLegacy.getString("description"));
            userDto.setId(userLegacy.getString("screen_name"));
            userDto.setName(userLegacy.getString("name"));
            userDto.setImgUrl(userLegacy.getString("profile_image_url_https"));
            userDto.setCreatedAt(DateUtil.conversionDate(userLegacy.getString("created_at")));
        } catch (Exception e) {
            log.error("parseUserDto error resultJson:{}", JSON.toJSONString(resultJson), e);
        }
        return userDto;
    }


}




