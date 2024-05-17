package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    private long width;
    private long height;
    private long durationMillis;
    private String videoUrl;
    private String videoPic;
    private String groupId;
    private long bitrate;
}
