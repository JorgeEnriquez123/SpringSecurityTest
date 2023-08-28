package com.jorge.securitytest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerTest {
    @GetMapping("/customloginpage")
    public String loginTest(){
        return "logintest";
    }

    @GetMapping("/loginfailure")
    public String loginFailTest(){
        return "failurelogin";
    }
}
