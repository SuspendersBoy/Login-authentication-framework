package com.example.Exception;

import com.example.entity.ApiResult;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

// 标记为 MVC 全局异常处理器
@ControllerAdvice
public class MvcExceptionHandler {

    // 处理参数缺失异常（@RequestParam 未传）
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ApiResult handleMissingParam(MissingServletRequestParameterException e) {
        String message = "缺少必要参数";
        return new ApiResult<>(400, message);
    }

    // 处理通用业务异常（可自定义）
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public  ApiResult  handleBusinessException(RuntimeException e) {
        String message = "业务异常";
        return new ApiResult<>(400, message);
    }

    // 处理所有未捕获的异常（兜底）
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResult handleAllException(Exception e) {
        String message = "系统异常";
        return new ApiResult<>(400, message);
    }
}