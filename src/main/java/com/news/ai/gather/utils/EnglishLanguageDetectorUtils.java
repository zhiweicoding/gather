package com.news.ai.gather.utils;

import org.springframework.stereotype.Component;

/**
 * @author zhiweicoding.xyz
 * @date 5/17/24
 * @email diaozhiwei2k@gmail.com
 */
public class EnglishLanguageDetectorUtils {

    public static boolean isMostlyEnglish(String text, double threshold) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        int englishCharCount = 0;
        int totalCharCount = 0;

        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                totalCharCount++;
                if (isEnglishChar(c)) {
                    englishCharCount++;
                }
            }
        }

        // Determine the threshold (e.g., 50%)
        return totalCharCount > 0 && (double) englishCharCount / totalCharCount >= threshold;
    }

    private static boolean isEnglishChar(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

}
