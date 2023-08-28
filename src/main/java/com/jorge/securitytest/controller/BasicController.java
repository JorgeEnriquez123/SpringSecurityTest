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
        return "You have signed in! - YOU ARE AN ADMIN";
    }

    @GetMapping("/p2")
    public String securedMessage2(){
        return "You have signed in! - YOU ARE AN USER";
    }

    @GetMapping("/profile")
    public Object profile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }

    @GetMapping("/home")
    public String homePage(){
        return "Welcome User! You've signed in";
        // I used a customSuccessHandler so this endpoint is not used unless sendRedirect is changed or defaultSuccessUrl is used.
    }

    @GetMapping("/goodbye")
    public String logoutPage(){
        return "You've just signed out. Bye!";
    }

    @GetMapping("/noaccess")
    public String notAllowed(){
        return "Hey, this page is for admins. you are not allowed to be here!";
    }
}
