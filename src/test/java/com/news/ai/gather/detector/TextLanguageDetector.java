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

        System.out.println("Text 1 is mostly English: " + isMostlyEnglish(text1,0.6));
        System.out.println("Text 2 is mostly English: " + isMostlyEnglish(text2,0.6));
    }
}
