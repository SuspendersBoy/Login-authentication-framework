package com.example.filter;

import com.alibaba.fastjson2.JSON;
import com.example.entity.JwtUserDetails;
import com.example.utils.JwtUtils;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.replace("Bearer ", "");
            //判断jwt 是否合法
            if (!jwtUtils.verifyToken(token)) throw new BadCredentialsException("无效的 JWT 令牌");
            //解析jwt 获取usr
            Map<String, Object> user = jwtUtils.parseToken(token);

            //权限存储的是json,所以需要转换
            JSONArray roleNamesArray = (JSONArray) user.get("rolenames");  // 权限（角色）
            List<GrantedAuthority> authorities = new ArrayList<>();
            for (Object roleObj : roleNamesArray) {
                String role = roleObj.toString(); // 取出 JSONArray 中的字符串（如 "ADMIN"）
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role)); // 拼接角色前缀
            }
            //创建认证对象
            UserDetails userDetails = new JwtUserDetails(user.get("username").toString(), user.get("password").toString(), authorities);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,           // 使用 UserDetails 作为主体
                            null,                  // JWT 场景下凭证为 null（已通过签名验证）
                            userDetails.getAuthorities()  // 从 UserDetails 获取权限
                    );

            //将认证信息注入 SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}