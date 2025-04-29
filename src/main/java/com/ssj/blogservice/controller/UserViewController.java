package com.ssj.blogservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    // 로그인 화면
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
