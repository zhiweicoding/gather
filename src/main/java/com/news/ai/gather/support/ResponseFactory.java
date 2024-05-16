package com.news.ai.gather.support;

import com.news.ai.gather.bean.entity.BaseResponse;
import lombok.Getter;

/**
 * @Created by zhiwei on 2022/3/27.
 */
public class ResponseFactory {

    public static <T> BaseResponse<T> success(T t) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setMsgCode(StatsEnum.SUCCESS.code);
        response.setMsgBody(t);
        response.setMsgInfo(StatsEnum.SUCCESS.name);
        return response;
    }

    public static <T> BaseResponse<T> fail(T t) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setMsgCode(StatsEnum.FAIL.code);
        response.setMsgBody(t);
        response.setMsgInfo(StatsEnum.FAIL.name);
        return response;
    }

    public static <T> BaseResponse<T> noToken(T t) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setMsgCode(StatsEnum.NO_TOKEN.code);
        response.setMsgBody(t);
        response.setMsgInfo(StatsEnum.NO_TOKEN.name);
        return response;
    }

    public static <T> BaseResponse<T> get(StatsEnum statsEnum, T t) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setMsgCode(statsEnum.code);
        response.setMsgBody(t);
        response.setMsgInfo(statsEnum.name);
        return response;
    }

    @Getter
    public enum StatsEnum {
        SUCCESS(10000, "成功"),
        FAIL(10001, "失败"),
        NO_TOKEN(10003, "没有登陆状态"),
        I_DO_NOT_KNOW(10002, "未知错误"),
        ERROR_URL(10005, "错误的地址");

        private final int code;
        private final String name;

        StatsEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        public String getName(int code) {
            for (StatsEnum c : StatsEnum.values()) {
                if (c.getCode() == code) {
                    return c.name;
                }
            }
            return "未知错误";
        }
    }
}
