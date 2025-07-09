package com.example.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Value("${spring.jwt.SECRET_KEY}")
    private  String SECRET_KEY;

    // Token有效期（毫秒），这里设置为1小时
    private final long EXPIRATION_TIME = 1000 * 60 * 60;

    /**
     * 生成JWT Token
     * @param userName,passWord 载荷数据
     * @return JWT Token字符串
     * @throws JOSEException 签名异常
     */
    public  String generateToken(String userName,String passWord,ArrayList<String> roleNames) throws JOSEException {
        // 将 String 类型的角色转换为 SimpleGrantedAuthority 集合
        List<GrantedAuthority> authorities = roleNames.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        //创建 User

        //将密钥转成字节码
        String key=Arrays.toString(SECRET_KEY.getBytes());
        // 创建JWS头，设置签名算法为HS256
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // 创建载荷
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .claim("username",userName)
                .claim("password",passWord)
                .claim("rolenames",roleNames)
                .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .build();

        // 创建JWS对象
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        // 创建签名器
        JWSSigner signer = new MACSigner(key);

        // 签名JWT
        signedJWT.sign(signer);

        // 生成JWT字符串
        return signedJWT.serialize();
    }

    /**
     * 验证JWT Token
     * @param token JWT Token字符串
     * @return 验证结果，true表示有效，false表示无效
     */
    public  boolean verifyToken(String token) {
        try {
            // 解析JWT字符串
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 创建验证器
            JWSVerifier verifier = new MACVerifier(Arrays.toString(SECRET_KEY.getBytes()));

            // 验证签名
            boolean isValidSignature = signedJWT.verify(verifier);

            // 验证过期时间
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

            boolean isNotExpired = expirationTime != null && expirationTime.after(new Date());

            return isValidSignature && isNotExpired;
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解析JWT Token获取载荷数据
     * @param token JWT Token字符串
     * @return 载荷数据Map
     */
    public  Map<String, Object> parseToken(String token) {
        try {
            // 解析JWT字符串
            SignedJWT signedJWT = SignedJWT.parse(token);

            // 获取载荷
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            //
            return claimsSet.getClaims();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}