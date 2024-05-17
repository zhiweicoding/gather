package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * {
 * "messages": [
 * {
 * "content": "You are a helpful assistant",
 * "role": "system"
 * },
 * {
 * "content": "Hi",
 * "role": "user"
 * }
 * ],
 * "model": "deepseek-coder",
 * "frequency_penalty": 0,
 * "max_tokens": 2048,
 * "presence_penalty": 0,
 * "stop": null,
 * "stream": false,
 * "temperature": 1,
 * "top_p": 1,
 * "logprobs": false,
 * "top_logprobs": null
 * }
 *
 * @author zhiweicoding.xyz
 * @date 5/17/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeepSeekChatDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -5786499424521955053L;

    private String model = "deepseek-chat";
    //介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其在已有文本中的出现频率受到相应的惩罚，降低模型重复相同内容的可能性。
    private int frequency_penalty = 0;

    private int max_tokens = 4096;
    //介于 -2.0 和 2.0 之间的数字。如果该值为正，那么新 token 会根据其是否已在已有文本中出现受到相应的惩罚，从而增加模型谈论新主题的可能性。
    private int presence_penalty = 0;
    //一个 string 或最多包含 4 个 string 的 list，在遇到这些词时，API 将停止生成更多的 token。
//    private String stop = null;
    //如果设置为 True，将会以 SSE（server-sent events）的形式以流式发送消息增量。消息流以 data: [DONE] 结尾。
    private boolean stream = false;
    //采样温度，介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。 我们通常建议可以更改这个值或者更改 top_p，但不建议同时对两者进行修改。
    private double temperature = 1.1;
    //作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。 我们通常建议修改这个值或者更改 temperature，但不建议同时对两者进行修改
    private int top_p = 1;
    //是否返回所输出 token 的对数概率。如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。
    private boolean logprobs = false;
    //一个介于 0 到 20 之间的整数 N，指定每个输出位置返回输出概率 top N 的 token，且返回这些 token 的对数概率。指定此参数时，logprobs 必须为 true。
//    private int top_logprobs = 1;
    /**
     * content : You are a helpful assistant
     * role : system
     */

    private List<MessagesBean> messages;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessagesBean implements Serializable {
        @Serial
        private static final long serialVersionUID = 4935940179755142170L;

        private String content;
        private String role;
    }
}
