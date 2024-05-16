package com.news.ai.gather.bean.model;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
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
@TableName("t_good")
public class GoodBean implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ASSIGN_UUID)
    private String goodId;
    private String goodTitle;
    private String goodBrief;
    private String scenePicUrl;
    private String listPicUrl;
    private double retailPrice;
    private int goodNumber;
    private String photoUrl;
    @TableField(exist = false)
    private List<String> photoUrlArray;
    @TableField(exist = false)
    private String symbolName;
    private String symbolId;
    @TableField(value = "is_new")
    private int isNew;
    @TableField(value = "is_chosen")
    private int isChosen;
    private int likeNum;
    private long createTime;
    private long modifyTime;
    private int isDelete;
    @TableField(exist = false)
    private int weight;

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        if (photoUrl != null && !photoUrl.isEmpty()) {
            this.photoUrlArray = JSON.parseArray(photoUrl, String.class);
        } else {
            this.photoUrlArray = new ArrayList<>();
        }
    }
}
