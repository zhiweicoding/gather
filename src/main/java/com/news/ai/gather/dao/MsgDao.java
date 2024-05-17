package com.news.ai.gather.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.news.ai.gather.bean.model.MsgBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhiwei
 */
@Component
public interface MsgDao extends BaseMapper<MsgBean> {

    void batchInsertMsg(@Param("list") List<MsgBean> ms);

}




