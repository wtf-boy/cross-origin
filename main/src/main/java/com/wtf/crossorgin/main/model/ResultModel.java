package com.wtf.crossorgin.main.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author wtf
 * @date 2023/8/17 10:59
 */
@Data
@AllArgsConstructor
public class ResultModel<T> {
    private int code;
    private String message;
    private T data;

    public static ResultModel ok() {
        return ok("请求成功");
    }

    public static <T> ResultModel success(T data) {
        return ok(200, "请求成功", data);
    }

    public static ResultModel ok(String message) {
        return ok(200, message);
    }

    public static ResultModel ok(int code, String message) {
        return ok(code, message, null);
    }

    public static <T> ResultModel ok(int code, String message, T data) {
        return new ResultModel(code, message, data);
    }
}
