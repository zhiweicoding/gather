package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author zhiweicoding.xyz
 * @date 5/17/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeepSeekModelDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 2593001375681078925L;

    private String id;
    private String object;
    private String owned_by;

}
