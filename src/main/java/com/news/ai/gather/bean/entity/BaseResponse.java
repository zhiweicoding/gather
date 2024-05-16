package com.news.ai.gather.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Created by zhiwei on 2022/3/11.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> implements Serializable {

    private String msgInfo;
    private int msgCode;//1000 success 10001 fail 1002 no auth
    private T msgBody;
    private int msgBodySize;

    /**
     * 判断bean 是否为空
     * @return
     */
    public boolean getIsEmpty() {
        return msgBody == null;
    }

}
