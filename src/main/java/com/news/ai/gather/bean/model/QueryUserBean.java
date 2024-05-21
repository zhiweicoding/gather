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
@TableName("t_query_user")
public class QueryUserBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = -7241507792877439601L;
    @TableId(type = IdType.AUTO)
    private String qId;
    private String userId;
    private String userAccount;
    private String qType;
    private int isDelete;

}
