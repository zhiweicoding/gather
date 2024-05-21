package com.news.ai.gather.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author zhiweicoding.xyz
 * @date 5/21/24
 * @email diaozhiwei2k@gmail.com
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectParams<Service> {
    private String key;
    private String url;
    private String txt;
    private String cookie;
    private String token;
    private Service service;

    public static final class CollectParamsBuilder<Service> {
        private String key;
        private String url;
        private String txt;
        private String cookie;
        private String token;
        private Service service;

        private CollectParamsBuilder() {
        }

        public static <Service> CollectParamsBuilder<Service> aCollectParams() {
            return new CollectParamsBuilder<>();
        }

        public CollectParamsBuilder<Service> key(String key) {
            this.key = key;
            return this;
        }

        public CollectParamsBuilder<Service> url(String url) {
            this.url = url;
            return this;
        }

        public CollectParamsBuilder<Service> txt(String txt) {
            this.txt = txt;
            return this;
        }

        public CollectParamsBuilder<Service> cookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public CollectParamsBuilder<Service> token(String token) {
            this.token = token;
            return this;
        }

        public CollectParamsBuilder<Service> service(Service service) {
            this.service = service;
            return this;
        }

        public CollectParams<Service> build() {
            CollectParams<Service> collectParams = new CollectParams<>();
            collectParams.setKey(key);
            collectParams.setUrl(url);
            collectParams.setTxt(txt);
            collectParams.setCookie(cookie);
            collectParams.setToken(token);
            collectParams.setService(service);
            return collectParams;
        }
    }
}
