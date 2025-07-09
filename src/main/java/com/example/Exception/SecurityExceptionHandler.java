package com.example.Exception;

import com.example.entity.ApiResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 处理认证异常（如未登录、无效令牌）
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 根据不同异常类型返回不同错误信息
        String message = "认证失败";
        if (authException instanceof BadCredentialsException) {
            message = "无效的凭证";
        } else if (authException instanceof InsufficientAuthenticationException) {
            System.out.println(authException.getMessage());
            message = "需要登录后访问";
        }

        ApiResult<?> result = ApiResult.error(HttpStatus.UNAUTHORIZED.value(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    // 处理授权异常（如权限不足）
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ApiResult<?> result = ApiResult.error(HttpStatus.FORBIDDEN.value(), "权限不足，拒绝访问");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}