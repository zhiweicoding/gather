
package com.news.ai.gather.bean.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qcloud.cos.transfer.Copy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_email")
public class EmailBean implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 6477450049241404849L;

    @TableId(type = IdType.AUTO)
    private int emailId;
    private String emailName;
    private String emailContent;
    private String emailFrom;
    private String emailTo;
    private String emailCopyTo;

    private LocalDateTime logTime;

    private int isDelete;
    private LocalDateTime createTime;
    private LocalDateTime modifyTime;
}
