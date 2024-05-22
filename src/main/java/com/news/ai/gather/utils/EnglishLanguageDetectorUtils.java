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

        // 移除 URL
        text = removeUrl(text);
        // 移除表情符号
        text = removeEmojis(text);
        text = removeEmojisV2(text);
        //remove new line
        text = removeNextLine(text);
        //such as: @openai
        text = removeTwitterHandles(text);

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

    private static String removeNextLine(String text) {
        return text.replaceAll("\\r?\\n", "");
    }

    private static String removeUrl(String text) {
        String urlRegex = "https?://\\S+\\s?";
        return text.replaceAll(urlRegex, "");
    }

    private static String removeEmojis(String text) {
        String emojiRegex = "[\\p{So}\\p{Cn}]";
        return text.replaceAll(emojiRegex, "");
    }

    private static String removeEmojisV2(String text) {
        // 更全面的表情符号正则表达式
        String emojiRegex = "[\uD83C\uD000-\uD83C\uDFFF]|[\uD83D\uD000-\uD83D\uDFFF]|[\uD83E\uD000-\uD83E\uDFFF]|[\u2600-\u26FF]|[\u2700-\u27BF]|[\uFE0F]";
        return text.replaceAll(emojiRegex, "");
    }

    private static String removeTwitterHandles(String text) {
        String twitterHandleRegex = "@\\w+";
        return text.replaceAll(twitterHandleRegex, "").replace("RT", "");
    }

}
