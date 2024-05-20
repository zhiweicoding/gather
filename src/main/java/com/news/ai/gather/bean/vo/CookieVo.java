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
public class CookieVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2245754032215091700L;

    private String cookie;
}
