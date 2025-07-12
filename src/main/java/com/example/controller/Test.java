package com.example.controller;

import com.example.service.UserService;
import com.example.utils.JwtUtils;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@ResponseBody
public class Test {
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;
    @PostMapping ("/auth/login")
    public String test( @RequestParam("username") String username,
                        @RequestParam("password") String password) throws Exception {
        ArrayList<String>arrayList=new ArrayList<>();
        arrayList.add(username);
        return "Bearer "+jwtUtils.generateToken(username,password,arrayList);
    }
    @PostMapping ("/test")
    public String test2() throws JOSEException {
        return "验证成功";
    }
}
