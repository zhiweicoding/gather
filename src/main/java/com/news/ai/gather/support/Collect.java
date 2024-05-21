package com.news.ai.gather.support;

import com.news.ai.gather.bean.dto.CollectParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author zhiweicoding.xyz
 * @date 5/21/24
 * @email diaozhiwei2k@gmail.com
 */
public interface Collect<Result, JobClass> {

    Result collect(CollectParams<JobClass> param);

}
