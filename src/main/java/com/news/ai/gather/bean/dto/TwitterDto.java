package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
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
    private String tweetId;
    private String fullText;
    private long createdAt;
    private String expanded;
    private List<String> photoUrlArray;
    private List<String> videoUrlArray;
    private UserDto userDto;


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
        private long createdAt;
    }
}
