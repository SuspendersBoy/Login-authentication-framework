package com.example.Exception;

import com.alibaba.fastjson2.JSON;
import com.example.entity.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    // 处理认证异常（如未登录、无效令牌）
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ApiResult<?> result = ApiResult.error(HttpStatus.UNAUTHORIZED.value(),"身份校验失败");
        response.getWriter().write(JSON.toJSONString(result));
    }

    // 处理授权异常（如权限不足）
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResult<?> result = ApiResult.error(HttpStatus.FORBIDDEN.value(), "权限不足，拒绝访问");
        response.getWriter().write(JSON.toJSONString(result));
    }
}