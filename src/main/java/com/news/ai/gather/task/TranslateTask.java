package com.news.ai.gather.task;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.news.ai.gather.bean.model.MsgBean;
import com.news.ai.gather.constants.TwitterKeyConstants;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.services.MsgService;
import com.news.ai.gather.services.TwitterService;
import com.news.ai.gather.support.DeepSeekSupport;
import com.news.ai.gather.support.RedisSupport;
import com.news.ai.gather.utils.EnglishLanguageDetectorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * redis cache overdue task
 *
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "translate.task", havingValue = "open")
public class TranslateTask {

    @Autowired
    private MsgService msgService;

    @Autowired
    private DeepSeekSupport deepSeekSupport;

    /**
     * checkCacheIsOverdue
     * each 60 min execute once
     */
    @Scheduled(fixedDelay = 60L * 60L * 1000L)
    public void translate() {
        log.info("start translate");
        List<MsgBean> ms = msgService.list(Wrappers.<MsgBean>lambdaQuery()
                .eq(MsgBean::getIsAlreadyTranslate, -1)
                .eq(MsgBean::getIsDelete, 1)
                .select(MsgBean::getMsgId, MsgBean::getMsgContent)
        );
        for (MsgBean m : ms) {
            String msgContent = m.getMsgContent();
            try {
                if (EnglishLanguageDetectorUtils.isMostlyEnglish(msgContent, 0.6)) {
                    String translateStr = deepSeekSupport.chat(msgContent, deepSeekSupport.getTranslatePrompt());
                    msgService.update(null, Wrappers.<MsgBean>lambdaUpdate()
                            .set(MsgBean::getTranslateMsgContent, translateStr)
                            .eq(MsgBean::getMsgId, m.getMsgId())
                    );
                }
            } catch (Exception e) {
                log.error("translate error,[{},{}]", m.getMsgId(), m.getMsgContent());
            }
        }

        log.info("end translate");
    }


}
