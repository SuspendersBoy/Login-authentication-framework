package com.example.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 统一API响应结果封装
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // 只序列化非空字段
public class ApiResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 状态码
    private int code;
    // 消息
    private String message;
    // 数据
    private T data;
    // 时间戳
    private String timestamp;

    // 构造函数
    public ApiResult() {
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public ApiResult(int code, String message) {
        this();
        this.code = code;
        this.message = message;
    }

    public ApiResult(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    // 成功响应（带数据）
    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<>(200, "操作成功", data);
    }

    // 成功响应（不带数据）
    public static <T> ApiResult<T> success() {
        return new ApiResult<>(200, "操作成功");
    }

    // 失败响应（自定义错误）
    public static <T> ApiResult<T> error(int code, String message) {
        return new ApiResult<>(code, message);
    }

    // 常见错误响应
    public static <T> ApiResult<T> unauthorized() {
        return new ApiResult<>(401, "未授权");
    }

    public static <T> ApiResult<T> forbidden() {
        return new ApiResult<>(403, "禁止访问");
    }

    public static <T> ApiResult<T> notFound() {
        return new ApiResult<>(404, "资源不存在");
    }

    public static <T> ApiResult<T> serverError() {
        return new ApiResult<>(500, "服务器内部错误");
    }

    // Getters and Setters
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}