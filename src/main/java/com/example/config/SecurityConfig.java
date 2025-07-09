package com.example.config;

import com.example.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;
    @Bean
    public SecurityFilterChain  securityFilterChain(HttpSecurity http) throws Exception {
        http
                //设置无状态
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //关闭跨域
                .csrf(AbstractHttpConfigurer::disable)
                //关闭表单登入
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests
                        -> authorizeHttpRequests
                        //放行以下匹配的资源
                        .requestMatchers( "/auth/login").permitAll()
                        //对所有资源,以及不存的资源进行拦截
                        .anyRequest().authenticated()
                )
                //security的全局异常处理器
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                //添加自定义过滤器
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
       return  http.build();
    }

}
