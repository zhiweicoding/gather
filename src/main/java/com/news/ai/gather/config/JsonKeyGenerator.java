package com.news.ai.gather.config;

import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 在使用spring framework框架时处理缓存
 * 1. 是多个参数时，使用fastjson.toJSONString(params)作为key
 * 2. 根据结果取md5
 * 3. 如果是null，不处理，返回NULL字符串
 *
 * @author zhiweicoding.xyz
 * @date 1/2/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
public class JsonKeyGenerator implements KeyGenerator {

    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    @Override
    public Object generate(Object target, Method method, Object... params) {
        try {
            List<Object> objArray = new ArrayList<>();
            for (Object param : params) {
                if (param != null) {
                    objArray.add(param);
                }
            }
            String jsonString = JSON.toJSONString(objArray,
                    JSONWriter.Feature.WriteMapNullValue,
                    JSONWriter.Feature.WriteNullListAsEmpty,
                    JSONWriter.Feature.WriteNullStringAsEmpty);
            log.debug("Json redis cache handler jsonString:{}", jsonString);
            if (jsonString.isEmpty()) {
                return "NULL";
            } else {
                return MD5.create().digestHex(jsonString, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            log.error("Json redis cache handler error:{}", e.getMessage(), e);
            return "NULL";
        }
    }
}
