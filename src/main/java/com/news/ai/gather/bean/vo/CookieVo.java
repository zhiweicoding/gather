package com.news.ai.gather.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zhiweicoding.xyz
 * @date 5/19/24
 * @email diaozhiwei2k@gmail.com
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class CookieVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 2245754032215091700L;

    private List<CookieItem> cookie;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (CookieItem item : cookie) {
            String name = item.getName();
            String value = item.getValue();
            sb.append(name).append("=").append(value).append("; ");
        }
        String sbStr = sb.toString();
        return sbStr.substring(0, sbStr.length() - 2);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class CookieItem implements Serializable {

        @Serial
        private static final long serialVersionUID = -3617628267518682425L;

        private String domain;
        private double expirationDate;
        private boolean hostOnly;
        private boolean httpOnly;
        private String name;
        private String path;
        private String sameSite;
        private boolean secure;
        private boolean session;
        private String storeId;
        private String value;

    }
}
