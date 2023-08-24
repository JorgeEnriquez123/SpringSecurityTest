package com.jorge.securitytest.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basic")
public class BasicController {
    @GetMapping
    public String publicMessage(){
        return "Public message - FOR ANONYMOUS USERS";
    }

    @GetMapping("/p1")
    public String securedMessage1(){
        return "Secured message 1 - FOR ADMINS";
    }

    @GetMapping("/p2")
    public String securedMessage2(){
        return "Secured message 2 - FOR USERS";
    }

    @GetMapping("/profile")
    public Object profile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }

    @GetMapping("/home")
    public String homePage(){
        return "Welcome! You've signed in";
    }

    @GetMapping("/goodbye")
    public String logoutPage(){
        return "You've just signed out. Bye!";
    }
}
