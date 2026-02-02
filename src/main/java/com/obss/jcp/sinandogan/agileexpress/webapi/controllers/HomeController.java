package com.obss.jcp.sinandogan.agileexpress.webapi.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

//    @GetMapping("/")
//    public String index() {
//        return "Welcome to the home page!";
//    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/home2")
    public String home2() {
        return "home2";
    }

    @GetMapping("/user")
    @ResponseBody
    public Object user(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes();
    }
}