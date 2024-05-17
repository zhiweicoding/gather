package com.news.ai.gather.bean.model;

import com.baomidou.mybatisplus.annotation.*;
import com.news.ai.gather.bean.model.typeHandler.Timestamp2LongTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_kol_info")
public class KolInfoBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 6477450049241404849L;

    @TableId(type = IdType.INPUT)
    private String kolId;

    private String kolName;
    private String kolImgUrl;
    private String description;

    private int isDelete;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;

}
