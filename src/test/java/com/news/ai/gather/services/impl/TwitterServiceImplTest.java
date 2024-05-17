package com.news.ai.gather.services.impl;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.support.CosSupport;
import com.qcloud.cos.model.PutObjectResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
@SpringBootTest()
@ActiveProfiles("dev")
class TwitterServiceImplTest {

    @Autowired
    private CosSupport cosSupport;

    @Test
    void splitRealInfo() {
        Map<String, Object> parse = JSON.parseObject("1", new TypeReference<>() {
        });
        TwitterServiceImpl twitterService = new TwitterServiceImpl();
        List<TwitterDto> twitterDtos = twitterService.splitRealInfo(parse);
        for (TwitterDto twitterDto : twitterDtos) {
            System.out.println(JSON.toJSONString(twitterDto));
        }
    }

    @Test
    public void test2() {
        //创建临时文件，然后下载url图片到本地
        String imageUrl = "https://pbs.twimg.com/ext_tw_video_thumb/1791107284015435777/pu/img/cnMqmzoFjEEhUDDG.jpg";
        try {
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                String uploadFileName = UUID.fastUUID().toString(true);
                cosSupport.upFile(uploadFileName, in);
                System.out.println("Image uploaded to object storage with filename: " + uploadFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        String imageUrl = "https://pbs.twimg.com/ext_tw_video_thumb/1791107284015435777/pu/img/cnMqmzoFjEEhUDDG.jpg";
        try {
            URL url = new URL(imageUrl);
            try (InputStream in = url.openStream()) {
                Path outputPath = Path.of("/Users/zhiwei/Desktop/1.jpg");
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File downloaded to: " + outputPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        String imageUrl = "https://pbs.twimg.com/ext_tw_video_thumb/1791107284015435777/pu/img/cnMqmzoFjEEhUDDG.jpg";

        System.out.println(cosSupport.upFile("img/twitter/", imageUrl));
    }


}