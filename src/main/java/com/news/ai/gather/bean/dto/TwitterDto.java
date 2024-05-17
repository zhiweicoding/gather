package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwitterDto {
    private String tweetId;//https://twitter.com/imxiaohu/status/
    private String fullText;
    private String translateMsgContent;
    private LocalDateTime createdAt;
    private List<String> photoUrlArray;
    private List<VideoDto> videoUrlArray;
    private UserDto userDto;
    private boolean retweet;
    private UserDto retweetUserDto;
    private String retweetId;

    private List<String> msgLinkArray;


    private TwitterDto subTwitterDto;

    //https://twitter.com/Amateur8888
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDto {
        private String description;
        private String id;
        private String name;
        private String imgUrl;
        private LocalDateTime createdAt;
    }
}
