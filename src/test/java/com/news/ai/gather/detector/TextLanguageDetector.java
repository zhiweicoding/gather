package com.news.ai.gather.detector;

/**
 * @author zhiweicoding.xyz
 * @date 5/17/24
 * @email diaozhiwei2k@gmail.com
 */
public class TextLanguageDetector {
    public static boolean isMostlyEnglish(String text, double threshold) {
        if (text == null || text.isEmpty()) {
            return false;
        }

        // 移除 URL
        text = removeUrl(text);
        System.out.println(text);
        // 移除表情符号
        text = removeEmojis(text);
        System.out.println(text);
        text = removeEmojisV2(text);
        System.out.println(text);
        text = removeNextLine(text);
        System.out.println(text);

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

    public static void main(String[] args) {
        String text1 = "LLaMA 3 70B is live on @HyperWriteAI!\n" +
                "\n" +
                "A custom version I've been working on for weeks.\n" +
                "\n" +
                "- writes more like a human than any other model\n" +
                "- can access real-time info\n" +
                "\n" +
                "Crushing every other model, including GPT-4, in user preference tests.\n" +
                "\n" +
                "\uD83E\uDD2FSeriously, look at the writing quality:";
        String text2 = "\uD83E\uDD23由我发起和联合国内多个头部开源社区共同举办的 ComfyUI 全球领导力峰会-中国分会场（深圳）将于本月底5.30-6.2开幕！\\n\\n❤\uFE0F本次开源社区的活动也接受个人赞助！！！！\\n❤\uFE0F公司与机构的赞助与合作也欢迎联系我们！！！\\n\uD83D\uDE97我的联系方式\\n邮箱：zhozho3965@gmail.com\\nV：见详情页\\n\\n✌\uFE0F聚集了国内 AIGC 众多核心开发者与创作者，目前报名已开启，欢迎大家来玩！我们深圳见！报名和详情请见下图或详情：https://t.co/MJ9t2XuDUq\\n\\n报名链接（截止日期 5.28）：https://t.co/qYfBr3MliD";
        String text3 = "这才是正确的AI交互界面\n" +
                "\n" +
                "\uD83E\uDEE1 https://t.co/LFkqaDx3Is";

        System.out.println("Text 1 is mostly English: " + isMostlyEnglish(text1, 0.6));
        System.out.println("Text 2 is mostly English: " + isMostlyEnglish(text2, 0.6));
        System.out.println("Text 3 is mostly English: " + isMostlyEnglish(text3, 0.6));
    }
}
