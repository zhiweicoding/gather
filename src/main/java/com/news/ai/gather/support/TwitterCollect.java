package com.news.ai.gather.support;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.news.ai.gather.bean.dto.CollectParams;
import com.news.ai.gather.bean.dto.TwitterDto;
import com.news.ai.gather.services.TwitterService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiweicoding.xyz
 * @date 5/21/24
 * @email diaozhiwei2k@gmail.com
 */
@Slf4j
public class TwitterCollect implements Collect<List<TwitterDto>, TwitterService> {

    @Override
    public List<TwitterDto> collect(CollectParams<TwitterService> param) {
        log.info("twitter collect start,[{}]", JSON.toJSONString(param));
        Map<String, Object> params = getTwitterParams(param.getUrl(), param.getTxt(), param.getKey(), param.getCookie(), param.getToken());
        TwitterService twitterService = param.getService();
        return twitterService.splitRealInfo(params);
    }

    /**
     * crawl twitter data
     *
     * @param urlPrefix crawl url,such as :<span>https://x.com/i/api/graphql/dqhyWBre4x9vdn60mIoexw/UserTweets</span>
     * @param key       user id
     * @param cookie    cookie
     * @param token     token json
     * @return UserTweets data
     */
    private Map<String, Object> getTwitterParams(String urlPrefix, String account, String key, String cookie, String token) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(urlPrefix).newBuilder();
        urlBuilder.addQueryParameter("variables", "{\"userId\":\"" + key + "\",\"count\":20,\"includePromotedContent\":true,\"withQuickPromoteEligibilityTweetFields\":true,\"withVoice\":true,\"withV2Timeline\":true}");
        urlBuilder.addQueryParameter("features", "{\"rweb_tipjar_consumption_enabled\":true,\"responsive_web_graphql_exclude_directive_enabled\":true,\"verified_phone_label_enabled\":false,\"creator_subscriptions_tweet_preview_api_enabled\":true,\"responsive_web_graphql_timeline_navigation_enabled\":true,\"responsive_web_graphql_skip_user_profile_image_extensions_enabled\":false,\"communities_web_enable_tweet_community_results_fetch\":true,\"c9s_tweet_anatomy_moderator_badge_enabled\":true,\"articles_preview_enabled\":true,\"tweetypie_unmention_optimization_enabled\":true,\"responsive_web_edit_tweet_api_enabled\":true,\"graphql_is_translatable_rweb_tweet_is_translatable_enabled\":true,\"view_counts_everywhere_api_enabled\":true,\"longform_notetweets_consumption_enabled\":true,\"responsive_web_twitter_article_tweet_consumption_enabled\":true,\"tweet_awards_web_tipping_enabled\":false,\"creator_subscriptions_quote_tweet_preview_enabled\":false,\"freedom_of_speech_not_reach_fetch_enabled\":true,\"standardized_nudges_misinfo\":true,\"tweet_with_visibility_results_prefer_gql_limited_actions_policy_enabled\":true,\"rweb_video_timestamps_enabled\":true,\"longform_notetweets_rich_text_read_enabled\":true,\"longform_notetweets_inline_media_enabled\":true,\"responsive_web_enhance_cards_enabled\":false}");
        urlBuilder.addQueryParameter("fieldToggles", "{\"withArticlePlainText\":false}");

        String url = urlBuilder.build().toString();

        JSONObject tokenJson = JSON.parseObject(token);

        Request request = new Request.Builder()
                .url(url)
                .header("accept", "*/*")
                .header("accept-language", "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7")
                .header("authorization", tokenJson.getString("authorization"))
                .header("content-type", tokenJson.getString("content-type"))
                .header("cookie", cookie)
                .header("priority", "u=1, i")
                .header("referer", "https://x.com/" + account)
                .header("sec-ch-ua", "Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"macOS\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36")
                .header("x-client-transaction-id", tokenJson.getString("x-client-transaction-id"))
                .header("x-client-uuid", tokenJson.getString("X-Client-UUID"))
                .header("x-csrf-token", tokenJson.getString("x-csrf-token"))
                .header("x-twitter-active-user", tokenJson.getString("x-twitter-active-user"))
                .header("x-twitter-auth-type", tokenJson.getString("x-twitter-auth-type"))
                .header("x-twitter-client-language", tokenJson.getString("x-twitter-client-language"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                log.info("Status Code: {}", response.code());
                log.info("Response: {}", responseBody);

                return JSON.parseObject(responseBody, new TypeReference<>() {
                });
            } else {
                log.error("请求失败，状态码: {}", response.code());
            }
        } catch (IOException e) {
            log.error("twitter collect error,[{}]", e.getMessage(), e);
        }
        return null;
    }


}
