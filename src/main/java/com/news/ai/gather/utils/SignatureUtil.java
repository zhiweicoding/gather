package com.news.ai.gather.utils;

import cn.hutool.crypto.digest.MD5;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * 生成订单微信签名
 *
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
public class SignatureUtil {
    public static String generateSignature(Map<String, String> data, String key) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String str : keyArray) {
            if (!str.equals("sign") && !data.get(str).trim().isEmpty()) {
                sb.append(str).append("=").append(data.get(str).trim()).append("&");
            }
        }
        sb.append("key=").append(key);
        String str = sb.toString();
        log.debug("生成订单微信签名： " + str);
        String resultStr = null;
        try {
            resultStr = MD5.create().digestHex(str, StandardCharsets.UTF_8).toUpperCase();
        } catch (Exception e) {
            log.error("生成微信签名error:{}", e.getMessage(), e);
        }
        return resultStr;
    }
}
