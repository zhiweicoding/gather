package com.news.ai.gather.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.news.ai.gather.bean.model.typeHandler.Timestamp2LongTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhiweicoding.xyz
 * @date 5/17/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_video")
public class VideoBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 8629254505281520853L;

    @TableId(type = IdType.AUTO)
    private int videoId;

    private long width;
    private long height;
    private long durationMillis;
    private long bitrate;
    private String videoGroupId;
    private String videoUrl;
    private String videoPicUrl;
    private int isDelete;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

}
