package com.news.ai.gather.services.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.bean.dto.VideoDto;
import com.news.ai.gather.bean.model.KolInfoBean;
import com.news.ai.gather.bean.model.MsgBean;
import com.news.ai.gather.bean.model.VideoBean;
import com.news.ai.gather.dao.KolInfoDao;
import com.news.ai.gather.dao.MsgDao;
import com.news.ai.gather.dao.VideoDao;
import com.news.ai.gather.services.TwitterService;
import com.news.ai.gather.support.CosSupport;
import com.news.ai.gather.support.DeepSeekSupport;
import com.news.ai.gather.utils.DateUtil;
import com.news.ai.gather.utils.EnglishLanguageDetectorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhiwei
 */
@Service(value = "twitterService")
@Slf4j
public class TwitterServiceImpl implements TwitterService {

    @Autowired
    private KolInfoDao kolInfoDao;

    @Autowired
    private CosSupport cosSupport;

    @Autowired
    private VideoDao videoDao;

    @Autowired
    private MsgDao msgDao;

    @Autowired
    private DeepSeekSupport deepSeekSupport;

    @Value("${twitter.userLink}")
    private String twitterUserLink;

    @Value("${twitter.detailLink}")
    private String twitterDetailLink;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<TwitterDto> splitRealInfo(Map<String, Object> params) {
        List<TwitterDto> dtoArray = new ArrayList<>();
        Map<String, Object> data = (Map<String, Object>) params.get("data");
        Map<String, Object> any = new HashMap<>();
        if (data.containsKey("threaded_conversation_with_injections_v2")) {
            any = (Map<String, Object>) data.get("threaded_conversation_with_injections_v2");
        } else if (data.containsKey("user")) {
            Map<String, Object> user = (Map<String, Object>) data.get("user");
            Map<String, Object> result = (Map<String, Object>) user.get("result");
            Map<String, Object> timelineV2 = (Map<String, Object>) result.get("timeline_v2");
            any = (Map<String, Object>) timelineV2.get("timeline");
        }
        List<Map<String, Object>> instructions = (List<Map<String, Object>>) any.get("instructions");
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
                log.debug("item:{}", item);
                JSONObject itemJson = JSON.parseObject(item);
                JSONObject content = itemJson.getJSONObject("content");
                if (content.containsKey("itemContent")) {
                    JSONObject itemContent = content.getJSONObject("itemContent");
                    String itemType = itemContent.getString("itemType");
                    if ("TimelineTweet".equals(itemType)) {
                        JSONObject resultJson = itemContent.getJSONObject("tweet_results").getJSONObject("result");
                        TwitterDto twitterDto = parseItemContent(resultJson);
                        dtoArray.add(twitterDto);
                    }
                }
            }
        }

        if (!dtoArray.isEmpty()) {
            String kolId = insertKol(dtoArray.get(0).getUserDto());
            List<MsgBean> msgBeans = packageTwitterData(kolId, dtoArray);

            log.debug("msgBeans:{}", JSON.toJSONString(msgBeans));
            log.debug("msgBeans size:{}", msgBeans.size());
            if (!msgBeans.isEmpty()) {
                msgDao.batchInsertMsg(msgBeans);
            }

        }


        return dtoArray;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MsgBean> packageTwitterData(String kolId, List<TwitterDto> dtoArray) {
        List<MsgBean> msgBeans = new ArrayList<>();
        for (TwitterDto dto : dtoArray) {
            List<MsgBean> itemArray = packageItemTwitter(kolId, dto, new ArrayList<>(), false);
            msgBeans.addAll(itemArray);
        }

        return msgBeans;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MsgBean> packageItemTwitter(String kolId, TwitterDto dto, List<MsgBean> msgLinked, boolean isSub) {
        MsgBean msgBean = new MsgBean();
        msgLinked.add(msgBean);
        msgBean.setMsgFromId(dto.getTweetId());
        msgBean.setIsSub(isSub ? 1 : -1);

        List<VideoDto> videoUrlArray = dto.getVideoUrlArray();
        if (videoUrlArray != null && !videoUrlArray.isEmpty()) {
            List<VideoBean> videoSelfArray = new ArrayList<>();
            for (VideoDto video : videoUrlArray) {
                VideoBean videoBean = insertVideo(video);
                videoSelfArray.add(videoBean);
            }
            msgBean.setMsgVideoList(JSON.toJSONString(videoSelfArray));
        }

        List<String> photoUrlArray = dto.getPhotoUrlArray();
        if (photoUrlArray != null && !photoUrlArray.isEmpty()) {
            List<String> photoSelfArray = new ArrayList<>();
            photoUrlArray.forEach(photoUrl -> {
                String cosUrl = cosSupport.getNameByRemote("img/twitter/", photoUrl);
                cosSupport.upFile("img/twitter/", photoUrl);
                photoSelfArray.add(cosUrl);
            });
            msgBean.setMsgImgList(JSON.toJSONString(photoSelfArray));
        }

        TwitterDto.UserDto retweetUserDto = dto.getRetweetUserDto();
        if (retweetUserDto != null && retweetUserDto.getId() != null) {
            String retweetId = insertKol(retweetUserDto);
            msgBean.setRetweetUserId(retweetId);
        }

        TwitterDto subTwitterDto = dto.getSubTwitterDto();
        if (subTwitterDto != null && subTwitterDto.getTweetId() != null) {
            msgBean.setSubMsgFromId(subTwitterDto.getTweetId());
            List<String> msgLinkArray = dto.getMsgLinkArray();
            if (msgLinkArray == null || msgLinkArray.isEmpty()) {
                msgLinkArray = new ArrayList<>();
            }
            msgLinkArray.add(dto.getTweetId());
            subTwitterDto.setMsgLinkArray(msgLinkArray);

            packageItemTwitter(kolId, subTwitterDto, msgLinked, true);
        }

        msgBean.setMsgType("twitter");
        String fullText = dto.getFullText();
        msgBean.setMsgContent(fullText);
        if (EnglishLanguageDetectorUtils.isMostlyEnglish(fullText, 0.6)) {
            String translateStr = deepSeekSupport.chat(fullText, deepSeekSupport.getTranslatePrompt());
            msgBean.setTranslateMsgContent(translateStr);
        }
        msgBean.setCreateUserId(kolId);

        if (dto.getRetweetId() != null && !dto.getRetweetId().isEmpty()) {
            msgBean.setRetweetFromId(dto.getRetweetId());
        }

        msgBean.setIsRetweet(dto.isRetweet() ? 1 : -1);
        msgBean.setMsgUrl(twitterDetailLink.replace("{userId}", kolId).replace("{restId}", dto.getTweetId()));
        msgBean.setMsgInfoRecord(JSON.toJSONString(dto));
        msgBean.setOrgCreateTime(dto.getCreatedAt());

        return msgLinked;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String insertKol(TwitterDto.UserDto userDto) {
        boolean existsKolUser = kolInfoDao.exists(Wrappers.<KolInfoBean>lambdaQuery()
                .eq(KolInfoBean::getKolId, userDto.getId()));
        if (!existsKolUser) {
            KolInfoBean kolInfoBean = new KolInfoBean();
            kolInfoBean.setKolId(userDto.getId());
            kolInfoBean.setKolName(userDto.getName());
            kolInfoBean.setDescription(userDto.getDescription());
            kolInfoBean.setKolImgUrl(userDto.getImgUrl());
            kolInfoBean.setIsDelete(1);
            kolInfoDao.insert(kolInfoBean);
            return kolInfoBean.getKolId();
        } else {
            return userDto.getId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VideoBean insertVideo(VideoDto videoDto) {
        List<VideoBean> vs = new ArrayList<>();
        LambdaQueryWrapper<VideoBean> existLambdaWrapper = Wrappers.<VideoBean>lambdaQuery()
                .eq(VideoBean::getVideoGroupId, videoDto.getGroupId())
                .eq(VideoBean::getBitrate, videoDto.getBitrate());
        boolean exists = videoDao.exists(existLambdaWrapper);
        VideoBean videoBean;
        if (!exists) {
            videoBean = new VideoBean();
            videoBean.setVideoGroupId(videoDto.getGroupId());
            String videoUrl = cosSupport.getNameByRemote("video/twitter/", videoDto.getVideoUrl());
            cosSupport.upFile("video/twitter/", videoDto.getVideoUrl());
            videoBean.setVideoUrl(videoUrl);
            String videoPic = cosSupport.getNameByRemote("img/twitter/", videoDto.getVideoPic());
            cosSupport.upFile("img/twitter/", videoDto.getVideoPic());
            videoBean.setVideoPicUrl(videoPic);
            videoBean.setBitrate(videoDto.getBitrate());
            videoBean.setHeight(videoDto.getHeight());
            videoBean.setWidth(videoDto.getWidth());
            videoBean.setDurationMillis(videoDto.getDurationMillis());
            videoBean.setIsDelete(1);
            videoDao.insert(videoBean);
        } else {
            videoBean = videoDao.selectOne(existLambdaWrapper);
        }
        return videoBean;
    }

    /**
     * parse each tweet item content
     *
     * @param resultJson
     * @return
     */
    private TwitterDto parseItemContent(JSONObject resultJson) {
        //tweet id
        String tweetId = resultJson.getString("rest_id");
        JSONObject legacy = resultJson.getJSONObject("legacy");

        TwitterDto twitterDto = new TwitterDto();
        twitterDto.setTweetId(tweetId);
        twitterDto.setUserDto(parseUserDto(resultJson));
        //msg created at
        LocalDateTime createdAt = DateUtil.conversionDate2LocalDateTime(legacy.getString("created_at"));
        twitterDto.setCreatedAt(createdAt);

        if (legacy.containsKey("retweeted_status_result")) {
            parseRetweet(twitterDto, legacy);
        } else {
            parsePrimordial(twitterDto, resultJson, legacy);
        }

        return twitterDto;
    }

    /**
     * parse primordial json twitter txt
     *
     * @param twitterDto
     * @param resultJson
     * @param legacy
     */
    private void parsePrimordial(TwitterDto twitterDto, JSONObject resultJson, JSONObject legacy) {
        twitterDto.setRetweet(false);
        if (legacy.containsKey("entities")) {
            parseVideoOrImg(legacy, twitterDto);
        }

        if (resultJson.containsKey("note_tweet")) {
            JSONObject noteTweet = resultJson.getJSONObject("note_tweet");
            if (noteTweet.containsKey("note_tweet_results")) {
                JSONObject noteTweetResults = noteTweet.getJSONObject("note_tweet_results");
                if (noteTweetResults.containsKey("result")) {
                    JSONObject result = noteTweetResults.getJSONObject("result");
                    String text = result.getString("text");
                    twitterDto.setFullText(text);
                }
            }
        } else {
            //full text
            String fullText = legacy.getString("full_text");
            twitterDto.setFullText(fullText);
        }
    }

    /**
     * parse retweet json twitter txt
     *
     * @param twitterDto
     * @param legacy
     */
    private void parseRetweet(TwitterDto twitterDto, JSONObject legacy) {
        twitterDto.setRetweet(true);
        JSONObject retweetedStatusResult = legacy.getJSONObject("retweeted_status_result");
        JSONObject result = retweetedStatusResult.getJSONObject("result");
        String reRestId = result.getString("rest_id");
        twitterDto.setRetweetId(reRestId);
        twitterDto.setRetweetUserDto(parseUserDto(result));

        JSONObject retweetLegacy = result.getJSONObject("legacy");
        if (retweetLegacy.containsKey("entities")) {
            parseVideoOrImg(retweetLegacy, twitterDto);
        }

        if (result.containsKey("note_tweet")) {
            JSONObject noteTweet = result.getJSONObject("note_tweet");
            if (noteTweet.containsKey("note_tweet_results")) {
                JSONObject noteTweetResults = noteTweet.getJSONObject("note_tweet_results");
                if (noteTweetResults.containsKey("result")) {
                    JSONObject noteResult = noteTweetResults.getJSONObject("result");
                    String noteText = noteResult.getString("text");
                    twitterDto.setFullText(noteText);
                }
            }
        } else {
            //full text
            String fullText = legacy.getString("full_text");
            twitterDto.setFullText(fullText);
        }

        //contains referenced tweets
        if (result.containsKey("quoted_status_result")) {
            JSONObject quotedStatusResult = result.getJSONObject("quoted_status_result").getJSONObject("result");
            TwitterDto subTwitterDto = parseItemContent(quotedStatusResult);
            twitterDto.setSubTwitterDto(subTwitterDto);
        }
    }

    /**
     * parse video or img
     *
     * @param legacy
     * @param twitterDto
     */
    private static void parseVideoOrImg(JSONObject legacy, TwitterDto twitterDto) {
        JSONObject entities = legacy.getJSONObject("entities");
        if (entities.containsKey("media")) {
            JSONArray mediaArray = entities.getJSONArray("media");
            List<String> photoUrlArray = new ArrayList<>();
            List<VideoDto> videoUrlArray = new ArrayList<>();
            for (int i = 0; i < mediaArray.size(); i++) {
                JSONObject media = mediaArray.getJSONObject(i);
                String type = media.getString("type");
                String mediaUrl = media.getString("media_url_https");
                if ("photo".equals(type)) {
                    photoUrlArray.add(mediaUrl);
                } else if ("video".equals(type)) {
                    JSONObject originalInfo = media.getJSONObject("original_info");
                    long width = originalInfo.getLong("width");
                    long height = originalInfo.getLong("height");
                    String mediaKey = media.getString("media_key");
                    JSONObject videoInfo = media.getJSONObject("video_info");
                    JSONArray variants = videoInfo.getJSONArray("variants");
                    long durationMillis = videoInfo.getLongValue("duration_millis");

                    VideoDto videoDto = new VideoDto();
                    videoDto.setGroupId(mediaKey);

                    variants.stream()
                            .filter(variant -> "video/mp4".equals(((JSONObject) variant).getString("content_type")))
                            .forEach(variant -> {
                                JSONObject vJson = (JSONObject) variant;
                                String videoUrl = vJson.getString("url");
                                long bitrate = vJson.getLongValue("bitrate");
                                int indexMp4 = videoUrl.indexOf("?tag");
                                videoUrl = videoUrl.substring(0, indexMp4);
                                videoDto.setVideoUrl(videoUrl);
                                videoDto.setVideoPic(mediaUrl);
                                videoDto.setBitrate(bitrate);
                                videoDto.setDurationMillis(durationMillis);
                                videoDto.setWidth(width);
                                videoDto.setHeight(height);
                                videoUrlArray.add(videoDto);
                            });
                }
            }
            twitterDto.setPhotoUrlArray(photoUrlArray);
            twitterDto.setVideoUrlArray(videoUrlArray);
        }
    }

    /**
     * parse user dto
     *
     * @param resultJson
     * @return
     */
    private TwitterDto.UserDto parseUserDto(JSONObject resultJson) {
        TwitterDto.UserDto userDto = new TwitterDto.UserDto();
        try {
            JSONObject userLegacy = resultJson.getJSONObject("core").getJSONObject("user_results").getJSONObject("result").getJSONObject("legacy");
            userDto.setDescription(userLegacy.getString("description"));
            userDto.setId(userLegacy.getString("screen_name"));
            userDto.setName(userLegacy.getString("name"));
            userDto.setImgUrl(userLegacy.getString("profile_image_url_https"));
            userDto.setCreatedAt(DateUtil.conversionDate2LocalDateTime(userLegacy.getString("created_at")));
        } catch (Exception e) {
            log.error("parseUserDto error resultJson:{}", JSON.toJSONString(resultJson), e);
        }
        return userDto;
    }


}




