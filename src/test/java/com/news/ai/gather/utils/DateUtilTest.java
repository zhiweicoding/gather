package com.news.ai.gather.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhiweicoding.xyz
 * @date 5/16/24
 * @email diaozhiwei2k@gmail.com
 */
class DateUtilTest {

    @Test
    void conversionDate() {
        long l = DateUtil.conversionDate2Long("Mon May 13 19:59:32 +0000 2024");
        System.out.println(l);
    }
}