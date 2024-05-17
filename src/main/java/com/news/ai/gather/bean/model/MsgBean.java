package com.news.ai.gather.bean.model;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.news.ai.gather.bean.model.typeHandler.Timestamp2LongTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_msg")
public class MsgBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1249130049263108537L;

    @TableId(type = IdType.AUTO)
    private String msgId;
    private String msgType;
    private String msgUrl;
    private String msgContent;
    private String translateMsgContent;
    private String createUserId;
    @TableField(exist = false)
    private KolInfoBean createUserKolInfo;

    private String retweetUserId;
    @TableField(exist = false)
    private KolInfoBean retweetUserKolInfo;

    private String retweetFromId;
    private int isRetweet;

    private int isSub;
    private String subMsgFromId;

    @TableField(exist = false)
    private List<MsgBean> msgLinkedArray = new ArrayList<>();

    private String msgFromId;

    private String msgVideoList;
    @TableField(exist = false)
    private List<VideoBean> videoArray = new ArrayList<>();

    private String msgImgList;
    @TableField(exist = false)
    private List<String> imgArray = new ArrayList<>();
    //long json text
    private String msgInfoRecord;

    private LocalDateTime orgCreateTime;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
    private int isDelete;


}