package com.news.ai.gather.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 5/19/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EmailMsgVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5330689151975524217L;

    private String title;
    private String content;
    private String to;

}
